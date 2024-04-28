package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.AutenticarUsuario;
import com.apostassa.aplicacao.usuario.AutenticarUsuarioDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.security.CriptografiaSenhaComBCrypt;
import com.apostassa.infra.security.TokenJWT;
import com.apostassa.infra.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AutenticarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private RepositorioDeUsuarioUserComJdbcPostgres repositorio;
	
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
    		DataSource pool = (DataSource) getServletContext().getAttribute("my-pool");
            this.repositorio = new RepositorioDeUsuarioUserComJdbcPostgres(pool.getConnection());
            super.service(request, response);
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new ServletException("Erro ao inicializar implementação do repositorio de usuario");
        }
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
		
		try {
			AutenticarUsuario autenticarUsuario = new AutenticarUsuario(repositorio, new CriptografiaSenhaComBCrypt(), new TokenJWT());
			AutenticarUsuarioDTO autenticarUsuarioDTO = deserializarAutenticarUsuarioDTO(requestBody);
			String tokenGerado = autenticarUsuario.executa(autenticarUsuarioDTO);
			
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName() == "token") {
						removerCookie(resp, "token");
					}
				}
			}
			
	        Map<String, String> jsonMap = new HashMap<>();
	        jsonMap.put("token", tokenGerado);
	        String json = new ObjectMapper().writeValueAsString(jsonMap);
			
			resp.getWriter().write(json);
			resp.setStatus(HttpServletResponse.SC_OK);
			
			Cookie cookie = new Cookie("token", tokenGerado);
			int dezDiasEmSegundos = 86400 * 5; // 86400 segundos em um dia
			cookie.setMaxAge(dezDiasEmSegundos);
			cookie.setPath("/");
			resp.addCookie(cookie);
		} catch (AutenticacaoException | ValidacaoException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar json de resposta");
		}
	}

	private AutenticarUsuarioDTO deserializarAutenticarUsuarioDTO(String requestBody) throws ValidacaoException {
		try {
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.registerModule(new JavaTimeModule());
	    
			return objectMapper.readValue(requestBody, AutenticarUsuarioDTO.class);
		} catch (JsonMappingException e) {
			throw new ValidacaoException(e.getCause().getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao processar json de resposta");
		}
	}
	
	private void removerCookie(HttpServletResponse resp, String nome) {
		Cookie cookie = new Cookie(nome, "");
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}
}
