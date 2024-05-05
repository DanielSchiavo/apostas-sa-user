package com.apostassa.aplicacao.categoria;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.categoria.Categoria;
import com.apostassa.dominio.categoria.RepositorioDeCategoriaUser;
import com.apostassa.dominio.categoria.subcategoria.RepositorioDeSubCategoriaUser;
import com.apostassa.dominio.categoria.subcategoria.SubCategoria;

import java.util.List;

public class PegarTodasCategorias {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeCategoriaUser repositorioDeCategoriaUser;

    private final CategoriaUserPresenter presenter;

    private final RepositorioDeSubCategoriaUser repositorioDeSubCategoriaUser;

    public PegarTodasCategorias(ProvedorConexao provedorConexao, RepositorioDeCategoriaUser repositorioDeCategoriaUser, CategoriaUserPresenter presenter, RepositorioDeSubCategoriaUser repositorioDeSubCategoriaUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeCategoriaUser = repositorioDeCategoriaUser;
        this.presenter = presenter;
        this.repositorioDeSubCategoriaUser = repositorioDeSubCategoriaUser;
    }

    public String executa(String ordenadoPor, String tipo) {
        try {
            List<Categoria> categorias = repositorioDeCategoriaUser.pegarTodasCategorias(ordenadoPor, tipo);

            categorias.forEach(c -> {
                String categoriaId = c.getId().toString();
                List<SubCategoria> subCategorias = repositorioDeSubCategoriaUser.pegarTodasSubCategoriasPorCategoriaId(categoriaId);
                subCategorias.forEach(c::adicionarSubCategoria);
            });

            provedorConexao.commitarTransacao();

            return presenter.respostaPegarTodasCategoria(categorias);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
