package com.apostassa.aplicacao.usuario;

import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class PegarDadosDoUsuarioPessoais {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	public PegarDadosDoUsuarioPessoais(RepositorioDeUsuarioUser repositorioDeUsuario) {
		this.repositorioDeUsuario = repositorioDeUsuario;
	}

	public String executa(String usuarioId) throws ValidacaoException, AutenticacaoException {
		try {
			Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPessoais(usuarioId);
			
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		    objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);
		    return objectMapper.writeValueAsString(usuario);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao processar objeto para JSON", e);
		}
	}
}
