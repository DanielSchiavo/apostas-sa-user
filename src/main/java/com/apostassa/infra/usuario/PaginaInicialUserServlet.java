package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.PegarDadosDoUsuarioPaginaInicial;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PaginaInicialUserServlet extends HttpServlet {

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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String usuarioId = req.getAttribute("usuarioId").toString();
		
		try {
			PegarDadosDoUsuarioPaginaInicial pegarDadosPaginaInicial = new PegarDadosDoUsuarioPaginaInicial(provedorConexaoJDBC, repositorio, adapter);
			String json = pegarDadosPaginaInicial.executa(usuarioId);
			resp.getWriter().write(json);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ValidacaoException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
