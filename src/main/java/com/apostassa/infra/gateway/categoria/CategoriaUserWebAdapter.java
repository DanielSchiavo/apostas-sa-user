package com.apostassa.infra.gateway.categoria;

import com.apostassa.aplicacao.gateway.categoria.CategoriaUserPresenter;
import com.apostassa.dominio.categoria.Categoria;
import com.apostassa.infra.util.JacksonUtil;

import java.util.List;

public class CategoriaUserWebAdapter implements CategoriaUserPresenter {

    @Override
    public String respostaPegarTodasCategoria(List<Categoria> categorias) {
        return JacksonUtil.serializadorNotNull(categorias);
    }
}
