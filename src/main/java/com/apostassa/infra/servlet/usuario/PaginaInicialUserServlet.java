package com.apostassa.infra.servlet.usuario;

import com.apostassa.aplicacao.usecase.usuario.PegarDadosDoUsuarioPaginaInicial;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.gateway.usuario.UsuarioUserWebAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/usuario/pagina-inicial")
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
		try {
			String usuarioCpf = req.getAttribute("usuarioCpf").toString();

			PegarDadosDoUsuarioPaginaInicial pegarDadosPaginaInicial = new PegarDadosDoUsuarioPaginaInicial(provedorConexaoJDBC, repositorio, adapter);
			String json = pegarDadosPaginaInicial.executa(usuarioCpf);

			resp.getWriter().write(json);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ValidacaoException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
