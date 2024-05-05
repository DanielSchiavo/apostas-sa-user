package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

public class UsuarioAlteraSeusDadosPessoais {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;

	private final UsuarioMapper usuarioMapper;
	
	public UsuarioAlteraSeusDadosPessoais(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(UsuarioAlteraSeusDadosPessoaisDTO dadosQueSeraoAlterados, String usuarioId) throws AutenticacaoException, AlterarUsuarioException {
		boolean jaVerificou = repositorioDeUsuario.verificarSeUsuarioJaConfirmouIdentidade(usuarioId);
		if (jaVerificou && (dadosQueSeraoAlterados.getCpf() != null || dadosQueSeraoAlterados.getRg() != null || 
							dadosQueSeraoAlterados.getNome() != null || dadosQueSeraoAlterados.getSobrenome() != null || 
							dadosQueSeraoAlterados.getDataNascimento() != null)) {
			throw new AlterarUsuarioException("Você não pode alterar seu cpf, rg, nome, sobrenome, data de nascimento porque você já confirmou sua identidade");
		}
		
		Usuario usuario = usuarioMapper.formatarUsuarioAlteraSeusDadosPessoaisDTOParaUsuario(dadosQueSeraoAlterados);
		usuario.setId(UUID.fromString(usuarioId));
		
		try {
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
