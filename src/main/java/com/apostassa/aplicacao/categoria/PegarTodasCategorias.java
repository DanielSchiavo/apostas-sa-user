package com.apostassa.aplicacao.categoria;

import com.apostassa.dominio.categoria.Categoria;
import com.apostassa.dominio.categoria.RepositorioDeCategoriaUser;
import com.apostassa.dominio.categoria.subcategoria.RepositorioDeSubCategoriaUser;
import com.apostassa.dominio.categoria.subcategoria.SubCategoria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

public class PegarTodasCategorias {

    private final RepositorioDeCategoriaUser repositorioDeCategoriaUser;

    private final RepositorioDeSubCategoriaUser repositorioDeSubCategoriaUser;

    public PegarTodasCategorias(RepositorioDeCategoriaUser repositorioDeCategoriaUser, RepositorioDeSubCategoriaUser repositorioDeSubCategoriaUser) {
        this.repositorioDeCategoriaUser = repositorioDeCategoriaUser;
        this.repositorioDeSubCategoriaUser = repositorioDeSubCategoriaUser;
    }

    public String executa(String ordenadoPor, String tipo) throws JsonProcessingException {
        List<Categoria> categorias = repositorioDeCategoriaUser.pegarTodasCategorias(ordenadoPor, tipo);

        categorias.forEach(c -> {
            String categoriaId = c.getId().toString();
            List<SubCategoria> subCategorias = repositorioDeSubCategoriaUser.pegarTodasSubCategoriasPorCategoriaId(categoriaId);
            subCategorias.forEach(c::adicionarSubCategoria);
        });

        repositorioDeCategoriaUser.commitarTransacao();

        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(categorias);
    }
}
