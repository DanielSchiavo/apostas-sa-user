package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.dominio.usuario.perfiljogador.PerfilJogador;
import org.mapstruct.factory.Mappers;


public class UsuarioAlteraSeuPerfilDeJogador {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	private UsuarioMapper usuarioMapper;
	
	public UsuarioAlteraSeuPerfilDeJogador(RepositorioDeUsuarioUser repositorioDeUsuario) {
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public void executa(UsuarioAlteraSeuPerfilDeJogadorDTO dadosQueSeraoAlterados, String usuarioId) throws AutenticacaoException, ValidacaoException, AlterarUsuarioException {
		PerfilJogador perfilJogador = usuarioMapper.formatarUsuarioAlteraSeuPerfilDeJogadorDTOParaUsuario(dadosQueSeraoAlterados);
		try {
		Usuario usuario = Usuario.builder().id(usuarioId)
										   .perfilJogador(perfilJogador)
										   .build();
		repositorioDeUsuario.usuarioAlteraSeuPerfilDeJogador(usuario);
		repositorioDeUsuario.commitarTransacao();
		} catch (ValidacaoException e) {
			e.printStackTrace();
			repositorioDeUsuario.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		} catch (AlterarUsuarioException e) {
			e.printStackTrace();
			repositorioDeUsuario.rollbackTransacao();
			throw new AlterarUsuarioException(e.getMessage());
		}
		
	}
	
}
