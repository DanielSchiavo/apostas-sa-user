package com.apostassa.aplicacao.categoria;

import com.apostassa.dominio.categoria.Categoria;

import java.util.List;

public interface CategoriaUserPresenter {

    public String respostaPegarTodasCategoria(List<Categoria> categorias);
}
