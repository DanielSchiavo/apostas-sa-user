package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.Token;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.security.CriptografiaBCrypt;
import com.apostassa.infra.util.Util;

import java.util.Map;


public class AutenticarUsuario {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	private final Token token;
	
	public AutenticarUsuario(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter, Token token) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.token = token;
	}

	public Map<String, String> executa(Usuario usuario) throws AutenticacaoException, ValidacaoException {
		try {
			Util.validar(usuario);

			Usuario usuarioPreAutenticado = repositorioDeUsuario.autenticarUsuario(usuario.getEmail());
			
			CriptografiaBCrypt.verificarSenha(usuarioPreAutenticado.getSenha(), usuario.getSenha());
			
			String tokenUsuarioAutenticado = token.gerarToken(usuarioPreAutenticado);
			provedorConexao.commitarTransacao();

			return presenter.respostaAutenticarUsuario(tokenUsuarioAutenticado);
		} catch (AutenticacaoException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new AutenticacaoException(e.getMessage());
		} catch (ValidacaoException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		} finally {
			provedorConexao.fecharConexao();
		}
	}
}
