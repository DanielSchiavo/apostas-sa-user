package com.apostassa.aplicacao.usuario;

import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.dominio.usuario.perfiljogador.PerfilJogador;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PegarDadosDoUsuarioPerfilDeJogador {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	public PegarDadosDoUsuarioPerfilDeJogador(RepositorioDeUsuarioUser repositorioDeUsuario) {
		this.repositorioDeUsuario = repositorioDeUsuario;
	}

	public String executa(String usuarioId) throws AutenticacaoException {
		PerfilJogador perfilJogador = repositorioDeUsuario.pegarDadosDoUsuarioPerfilDeJogador(usuarioId);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(perfilJogador);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao processar objeto para JSON");
		}
	}
	
}
