package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.UsuarioAlteraSuaSenha;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSuaSenhaDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.security.CriptografiaSenhaComBCrypt;
import com.apostassa.infra.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class AlterarSenhaServlet extends HttpServlet {

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
    	
    	try {
    		String email = (String) req.getAttribute("email");
    		UsuarioAlteraSuaSenhaDTO usuarioAlteraSuaSenhaDTO = deserializarUsuarioAlteraSuaSenhaDTO(requestBody);
    		
    		UsuarioAlteraSuaSenha usuarioAlteraSuaSenha = new UsuarioAlteraSuaSenha(repositorio, new CriptografiaSenhaComBCrypt());
    		usuarioAlteraSuaSenha.executa(usuarioAlteraSuaSenhaDTO, email);
    		
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("Senha alterada com sucesso!");
		} catch (AlterarUsuarioException | AutenticacaoException | ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
			return;
		}
    }

	private UsuarioAlteraSuaSenhaDTO deserializarUsuarioAlteraSuaSenhaDTO(String requestBody) {
		try {
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    
			return objectMapper.readValue(requestBody, UsuarioAlteraSuaSenhaDTO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao mapear json de resposta");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao processar json de resposta");
		}
	}
}
