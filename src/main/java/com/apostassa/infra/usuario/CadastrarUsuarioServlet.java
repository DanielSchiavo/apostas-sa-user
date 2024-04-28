package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.CadastrarUsuario;
import com.apostassa.aplicacao.usuario.CadastrarUsuarioDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.infra.security.CriptografiaSenhaComBCrypt;
import com.apostassa.infra.util.GeradorUUIDImpl;
import com.apostassa.infra.util.Util;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class CadastrarUsuarioServlet extends HttpServlet {

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
	    	CadastrarUsuarioDTO cadastrarUsuarioDTO = deserializarCadastrarUsuarioDTO(requestBody);
	    	CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(this.repositorio, new CriptografiaSenhaComBCrypt(), new GeradorUUIDImpl());
	    	String mensagem = cadastrarUsuario.executa(cadastrarUsuarioDTO);
			resp.getWriter().write(mensagem);
			resp.setStatus(HttpServletResponse.SC_CREATED);
			return;
		} catch (ValidacaoException e) {
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	private CadastrarUsuarioDTO deserializarCadastrarUsuarioDTO(String requestBody) throws ValidacaoException {
		try {
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.registerModule(new JavaTimeModule());
		    objectMapper.configOverride(LocalDate.class).setFormat(JsonFormat.Value.forPattern("dd/MM/yyyy"));
		    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    
			return objectMapper.readValue(requestBody, CadastrarUsuarioDTO.class);
		} catch (JsonMappingException e) {
			throw new ValidacaoException(e.getCause().getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao processar json de resposta");
		}
	}

}
