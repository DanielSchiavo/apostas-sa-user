package com.apostassa.aplicacao.gateway.evento;

import com.apostassa.dominio.evento.Evento;

import java.util.List;
import java.util.UUID;

public interface RepositorioDeEvento {

	public Evento pegarEventoPorId(UUID id);

	public List<Evento> pegarTodosEventos();

	public List<Evento> pegarTodosEventosPorSubCategoriaId(String subCategoriaId);
}
