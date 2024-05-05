package com.apostassa.infra.usuario;

import com.apostassa.aplicacao.usuario.CadastrarUsuario;
import com.apostassa.aplicacao.usuario.CadastrarUsuarioDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.security.CriptografiaSenhaComBCrypt;
import com.apostassa.infra.util.GeradorUUIDImpl;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class CadastrarUsuarioUserServlet extends HttpServlet {

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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = Util.pegarJsonCorpoDaRequisicao(req);

	    try {
	    	CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(provedorConexaoJDBC, repositorio, adapter, new CriptografiaSenhaComBCrypt(), new GeradorUUIDImpl());

			CadastrarUsuarioDTO cadastrarUsuarioDTO = (CadastrarUsuarioDTO) JacksonUtil.deserializar(requestBody, CadastrarUsuarioDTO.class);
			Map<String, String> respostaCadastrarUsuario = cadastrarUsuario.executa(cadastrarUsuarioDTO);

			resp.getWriter().write(respostaCadastrarUsuario.get("mensagem"));
			resp.setStatus(HttpServletResponse.SC_CREATED);
		} catch (ValidacaoException e) {
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
