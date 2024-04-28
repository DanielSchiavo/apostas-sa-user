package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;


public class PegarDadosDoUsuarioPaginaInicial {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	private UsuarioMapper usuarioMapper;
	
	public PegarDadosDoUsuarioPaginaInicial(RepositorioDeUsuarioUser repositorioDeUsuario) {
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(String usuarioId) throws ValidacaoException, AutenticacaoException {
		Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPaginaInicial(usuarioId);
		UsuarioPaginaInicialDTO usuarioPaginaInicialDTO = usuarioMapper.formatarUsuarioParaUsuarioPaginaInicialDTO(usuario);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(usuarioPaginaInicialDTO);
			return json;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao processar json de resposta");
		}
	}
	
}
