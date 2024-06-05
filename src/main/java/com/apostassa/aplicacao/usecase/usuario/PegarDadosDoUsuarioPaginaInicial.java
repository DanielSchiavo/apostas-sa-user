package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;


public class PegarDadosDoUsuarioPaginaInicial {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;

	public PegarDadosDoUsuarioPaginaInicial(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
	}

	public String executa(String usuarioCpf) throws ValidacaoException {
		try {
			Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPaginaInicial(usuarioCpf);

			return presenter.respostaPegarDadosDoUsuarioPaginaInicial(usuario);
		} finally {
			provedorConexao.fecharConexao();
		}
	}
	
}
