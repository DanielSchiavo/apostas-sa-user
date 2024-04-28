package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.PegarDadosDoUsuarioPessoais;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoais;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoaisDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
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

public class DadosPessoaisServlet extends HttpServlet {

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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PegarDadosDoUsuarioPessoais pegarDadosPessoais = new PegarDadosDoUsuarioPessoais(repositorio);
		try {
			String usuarioId = (String) req.getAttribute("usuarioId");
			String json = pegarDadosPessoais.executa(usuarioId);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(json);
			return;
		} catch (AutenticacaoException | ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
			return;
		}
	}
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
    	
    	try {
    		String usuarioId = (String) req.getAttribute("usuarioId");
    		UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO = deserializarUsuarioAlteraSeusDadosPessoaisDTO(requestBody);
    		
    		UsuarioAlteraSeusDadosPessoais usuarioAlteraSeusDadosPessoais = new UsuarioAlteraSeusDadosPessoais(repositorio);
    		usuarioAlteraSeusDadosPessoais.executa(usuarioAlteraSeusDadosPessoaisDTO, usuarioId);
    		
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("Dados pessoais alterado com sucesso!");
		} catch (ValidacaoException | AlterarUsuarioException | AutenticacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
			return;
		}
    }

	private UsuarioAlteraSeusDadosPessoaisDTO deserializarUsuarioAlteraSeusDadosPessoaisDTO(String requestBody) throws ValidacaoException {
		try {
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.registerModule(new JavaTimeModule());
		    objectMapper.configOverride(LocalDate.class).setFormat(JsonFormat.Value.forPattern("dd/MM/yyyy"));
		    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    
			return objectMapper.readValue(requestBody, UsuarioAlteraSeusDadosPessoaisDTO.class);
		} catch (JsonMappingException e) {
			throw new ValidacaoException(e.getCause().getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao processar json de resposta");
		}
	}
}
