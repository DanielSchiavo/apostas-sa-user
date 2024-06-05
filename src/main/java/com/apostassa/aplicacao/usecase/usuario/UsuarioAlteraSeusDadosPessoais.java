package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.util.Util;

public class UsuarioAlteraSeusDadosPessoais {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;

	public UsuarioAlteraSeusDadosPessoais(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
	}

	public String executa(Usuario usuario, String usuarioCpf) throws AutenticacaoException, AlterarUsuarioException {
		try {
			Util.validar(usuario);

			//O usuario pode alterar além desses campos o Celular e o Email se jaVerificou for true
			boolean jaVerificou = repositorioDeUsuario.verificarSeUsuarioJaConfirmouIdentidade(usuarioCpf);
			if (jaVerificou && (usuario.getCpf() != null || usuario.getRg() != null || usuario.getNome() != null ||
								usuario.getSobrenome() != null || usuario.getDataNascimento() != null)) {
				throw new AlterarUsuarioException("Você não pode alterar seu cpf, rg, nome, sobrenome ou data de nascimento porque você já confirmou sua identidade");
			}

			repositorioDeUsuario.usuarioAlteraSeusDadosPessoais(usuario);
			provedorConexao.commitarTransacao();
			return presenter.respostaUsuarioAlteraSeusDadosPessoais(usuario);
		} catch (AlterarUsuarioException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new AlterarUsuarioException(e.getMessage());
		} finally {
			provedorConexao.fecharConexao();
		}
	}
}
