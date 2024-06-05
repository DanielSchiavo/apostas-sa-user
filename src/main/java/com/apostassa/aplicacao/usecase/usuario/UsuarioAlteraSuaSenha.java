package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.security.CriptografiaBCrypt;

public class UsuarioAlteraSuaSenha {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	public UsuarioAlteraSuaSenha(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
	}

	public String executa(String senhaAtual, String senhaNova, String email) throws AutenticacaoException, AlterarUsuarioException, ValidacaoException {
		try {
			Usuario usuarioPreAutenticado = repositorioDeUsuario.autenticarUsuario(email);
			
			CriptografiaBCrypt.verificarSenha(usuarioPreAutenticado.getSenha(), senhaAtual);
			String senhaNovaCriptografada = CriptografiaBCrypt.criptografarSenha(senhaNova);
			repositorioDeUsuario.usuarioAlteraSuaSenha(senhaNovaCriptografada, email);
			provedorConexao.commitarTransacao();

			return presenter.respostaUsuarioAlteraSuaSenha();
		} catch (AlterarUsuarioException e) {
			provedorConexao.rollbackTransacao();
			e.printStackTrace();
			throw new AlterarUsuarioException(e.getMessage());
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
