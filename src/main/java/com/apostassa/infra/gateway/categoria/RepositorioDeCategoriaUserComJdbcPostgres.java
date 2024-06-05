package com.apostassa.infra.gateway.categoria;

import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.categoria.Categoria;
import com.apostassa.aplicacao.gateway.categoria.RepositorioDeCategoriaUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioDeCategoriaUserComJdbcPostgres implements RepositorioDeCategoriaUser {

    private final Connection connection;

    public RepositorioDeCategoriaUserComJdbcPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Categoria pegarCategoriaPorId(String categoriaId) throws ValidacaoException {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String nome = rs.getString("nome");
                    String icone = rs.getString("icone");

                    Categoria categoria = Categoria.builder().id(UUID.fromString(id))
                                                            .nome(nome)
                                                            .icone(icone).build();

                    return categoria;

                } else {
                    throw new ValidacaoException("Não existe categoria com esse id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categoria> pegarTodasCategorias(String ordenadoPor, String tipo) {
        StringBuilder sql = new StringBuilder("SELECT * FROM categorias WHERE ativo = true");

        if (ordenadoPor != null) {
            sql.append(" ORDER BY ").append(ordenadoPor);
        }
        if (tipo != null) {
            sql.append(" ").append(tipo);
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Categoria> listaCategoria = new ArrayList<>();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nome = rs.getString("nome");
                    String icone = rs.getString("icone");

                    Categoria categoria = Categoria.builder().id(UUID.fromString(id))
                            .nome(nome)
                            .icone(icone)
                            .subCategorias(new ArrayList<>()).build();

                    listaCategoria.add(categoria);
                }
                return listaCategoria;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
