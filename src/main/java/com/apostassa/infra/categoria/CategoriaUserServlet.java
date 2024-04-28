package com.apostassa.infra.categoria;

import com.apostassa.aplicacao.categoria.PegarTodasCategorias;
import com.apostassa.infra.categoria.subcategoria.RepositorioDeSubCategoriaUserComJdbcPostgres;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class CategoriaUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private RepositorioDeCategoriaUserComJdbcPostgres repositorio;
	
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
    		DataSource pool = (DataSource) getServletContext().getAttribute("my-pool");
            this.repositorio = new RepositorioDeCategoriaUserComJdbcPostgres(pool.getConnection());
            super.service(request, response);
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new ServletException("Erro ao inicializar implementação do repositorio de usuario");
        }
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ordenadoPor = req.getParameter("ordenadoPor");
		String tipo = req.getParameter("tipo");

		PegarTodasCategorias pegarTodasCategorias = new PegarTodasCategorias(repositorio, new RepositorioDeSubCategoriaUserComJdbcPostgres(repositorio.getConnection()));
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
