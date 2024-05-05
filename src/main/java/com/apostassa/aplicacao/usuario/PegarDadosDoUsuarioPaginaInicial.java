package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import org.mapstruct.factory.Mappers;


public class PegarDadosDoUsuarioPaginaInicial {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;

	private final UsuarioMapper usuarioMapper;
	
	public PegarDadosDoUsuarioPaginaInicial(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(String usuarioId) throws ValidacaoException {
		try {
			Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPaginaInicial(usuarioId);

			return presenter.respostaPegarDadosDoUsuarioPaginaInicial(usuario);
		} finally {
			provedorConexao.fecharConexao();
		}
	}
	
}
