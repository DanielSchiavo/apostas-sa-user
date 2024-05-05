package com.apostassa.infra.categoria;

import com.apostassa.aplicacao.categoria.PegarTodasCategorias;
import com.apostassa.infra.categoria.subcategoria.RepositorioDeSubCategoriaUserComJdbcPostgres;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CategoriaUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProvedorConexaoJDBC provedorConexaoJDBC;

	private RepositorioDeCategoriaUserComJdbcPostgres repositorio;

	private CategoriaUserWebAdapter adapter;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.provedorConexaoJDBC = InicializadorConexao.executa(request);
		this.repositorio = new RepositorioDeCategoriaUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
		this.adapter = new CategoriaUserWebAdapter();
		super.service(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ordenadoPor = req.getParameter("ordenadoPor");
		String tipo = req.getParameter("tipo");

		PegarTodasCategorias pegarTodasCategorias = new PegarTodasCategorias(provedorConexaoJDBC, repositorio, adapter, new RepositorioDeSubCategoriaUserComJdbcPostgres(provedorConexaoJDBC.getConexao()));
		try {
			String jsonCategorias = pegarTodasCategorias.executa(ordenadoPor, tipo);

			resp.getWriter().write(jsonCategorias);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (JsonProcessingException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
	}
}
