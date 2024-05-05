package com.apostassa.infra.usuario.perfilparticipante;

import com.apostassa.aplicacao.usuario.perfilparticipante.AlterarPerfilParticipante;
import com.apostassa.aplicacao.usuario.perfilparticipante.AlterarPerfilParticipanteDTO;
import com.apostassa.aplicacao.usuario.perfilparticipante.CriarPerfilParticipante;
import com.apostassa.aplicacao.usuario.perfilparticipante.PegarDadosPerfilParticipante;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.AlterarPerfilParticipanteException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PerfilParticipanteUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProvedorConexaoJDBC provedorConexaoJDBC;

	private RepositorioDePerfilParticipanteUserComJdbcPostgres repositorio;

	private PerfilParticipanteUserWebAdapter adapter;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.provedorConexaoJDBC = InicializadorConexao.executa(request);
		this.repositorio = new RepositorioDePerfilParticipanteUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
		this.adapter = new PerfilParticipanteUserWebAdapter();
		super.service(request, response);
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String usuarioId = req.getAttribute("usuarioId").toString();
		try {
			PegarDadosPerfilParticipante pegarDadosPerfilParticipante = new PegarDadosPerfilParticipante(provedorConexaoJDBC, repositorio, adapter);
			String json = pegarDadosPerfilParticipante.executa(usuarioId);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(json);
		} catch (ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
        }
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String usuarioId = req.getAttribute("usuarioId").toString();
	    try {
	    	CriarPerfilParticipante criarPerfilParticipante = new CriarPerfilParticipante(provedorConexaoJDBC, repositorio, adapter);
	    	String respostaCriarPerfilParticipante = criarPerfilParticipante.executa(usuarioId);

			resp.getWriter().write(respostaCriarPerfilParticipante);
			resp.setStatus(HttpServletResponse.SC_CREATED);
		} catch (ValidacaoException e) {
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
		String usuarioId = req.getAttribute("usuarioId").toString();
		try {
			AlterarPerfilParticipante alterarPerfilParticipante = new AlterarPerfilParticipante(provedorConexaoJDBC, repositorio, adapter);

			AlterarPerfilParticipanteDTO alterarPerfilParticipanteDTO = (AlterarPerfilParticipanteDTO) JacksonUtil.deserializar(requestBody, AlterarPerfilParticipanteDTO.class);
			String respostaAlterarPerfilParticipante = alterarPerfilParticipante.executa(alterarPerfilParticipanteDTO, usuarioId);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaAlterarPerfilParticipante);
		} catch (AlterarPerfilParticipanteException | ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
        }
    }
}
