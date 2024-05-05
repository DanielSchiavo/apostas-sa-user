package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.PegarDadosDoUsuarioPessoais;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoais;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoaisDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DadosPessoaisUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProvedorConexaoJDBC provedorConexaoJDBC;

	private RepositorioDeUsuarioUserComJdbcPostgres repositorio;

	private UsuarioUserWebAdapter adapter;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.provedorConexaoJDBC = InicializadorConexao.executa(request);
		this.repositorio = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
		this.adapter = new UsuarioUserWebAdapter();
		super.service(request, response);
	}
    
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PegarDadosDoUsuarioPessoais pegarDadosPessoais = new PegarDadosDoUsuarioPessoais(provedorConexaoJDBC, repositorio, adapter);
		try {
			String usuarioId = req.getAttribute("usuarioId").toString();
			String respostaPegarDadosDoUsuarioPessoais = pegarDadosPessoais.executa(usuarioId);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaPegarDadosDoUsuarioPessoais);
		} catch (ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
	}
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
    	
    	try {
    		String usuarioId = req.getAttribute("usuarioId").toString();
			UsuarioAlteraSeusDadosPessoais usuarioAlteraSeusDadosPessoais = new UsuarioAlteraSeusDadosPessoais(provedorConexaoJDBC, repositorio, adapter);

			UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO = (UsuarioAlteraSeusDadosPessoaisDTO) JacksonUtil.deserializar(requestBody, UsuarioAlteraSeusDadosPessoaisDTO.class);
			String respostaUsuarioAlteraSeusDadosPessoais = usuarioAlteraSeusDadosPessoais.executa(usuarioAlteraSeusDadosPessoaisDTO, usuarioId);


			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaUsuarioAlteraSeusDadosPessoais);
		} catch (ValidacaoException | AlterarUsuarioException | AutenticacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
    }
}
