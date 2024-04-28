package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.PegarDadosDoUsuarioPaginaInicial;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class PaginaInicialServlet extends HttpServlet {

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
		String usuarioId = (String) req.getAttribute("usuarioId");
		
		try {
			PegarDadosDoUsuarioPaginaInicial pegarDadosPaginaInicial = new PegarDadosDoUsuarioPaginaInicial(repositorio);
			String json = pegarDadosPaginaInicial.executa(usuarioId);
			resp.getWriter().write(json);
			resp.setStatus(HttpServletResponse.SC_OK);
			return;
		} catch (ValidacaoException | AutenticacaoException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}
}
