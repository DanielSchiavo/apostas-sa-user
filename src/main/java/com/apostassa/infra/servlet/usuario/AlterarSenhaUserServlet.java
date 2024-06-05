package com.apostassa.infra.servlet.usuario;

import com.apostassa.aplicacao.usecase.usuario.UsuarioAlteraSuaSenha;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.gateway.usuario.UsuarioUserWebAdapter;
import com.apostassa.infra.servlet.usuario.dto.UsuarioAlterarSenhaDTO;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/usuario/alterar-senha")
public class AlterarSenhaUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProvedorConexaoJDBC provedorConexaoJDBC;

	private RepositorioDeUsuarioUserComJdbcPostgres repositorio;

	private UsuarioUserWebAdapter adapter;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.provedorConexaoJDBC = InicializadorConexao.executa(request);
		this.repositorio = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
		adapter = new UsuarioUserWebAdapter();
		super.service(request, response);
	}
	
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String requestBody = Util.pegarJsonCorpoDaRequisicao(req);

			String email = req.getAttribute("email").toString();
			UsuarioAlterarSenhaDTO usuarioAlteraSuaSenhaDTO = (UsuarioAlterarSenhaDTO) JacksonUtil.deserializar(requestBody, UsuarioAlterarSenhaDTO.class);
			Util.validar(usuarioAlteraSuaSenhaDTO);

			UsuarioAlteraSuaSenha usuarioAlteraSuaSenha = new UsuarioAlteraSuaSenha(provedorConexaoJDBC, repositorio, adapter);
			String respostaUsuarioAlteraSuaSenha = usuarioAlteraSuaSenha.executa(usuarioAlteraSuaSenhaDTO.getSenhaAtual(), usuarioAlteraSuaSenhaDTO.getSenhaNova(), email);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaUsuarioAlteraSuaSenha);
		} catch (AlterarUsuarioException | AutenticacaoException | ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
    }
}
