package com.apostassa.infra.categoria.subcategoria;

import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.categoria.subcategoria.RepositorioDeSubCategoriaUser;
import com.apostassa.dominio.categoria.subcategoria.SubCategoria;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioDeSubCategoriaUserComJdbcPostgres implements RepositorioDeSubCategoriaUser {

	private final Connection connection;

	public RepositorioDeSubCategoriaUserComJdbcPostgres(Connection connection) {
		this.connection = connection;
	}

	@Override
	public SubCategoria pegarSubCategoriaPorId(String subCategoriaId) throws ValidacaoException {
		String sql = "SELECT * FROM sub_categorias WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, subCategoriaId);
			try (ResultSet rs = ps.executeQuery()) {
				boolean encontrou = rs.next();
				if (!encontrou) {
					throw new ValidacaoException("Não existe sub-categoria com esse id");
				}
				String id = rs.getString("id");
				String nome = rs.getString("nome");
				String icone = rs.getString("icone");

				return SubCategoria.builder()
									.id(UUID.fromString(id))
									.nome(nome)
									.icone(icone).build();

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public List<SubCategoria> pegarTodasSubCategorias() throws ValidacaoException {
		String sql = "SELECT * FROM sub_categorias";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				List<SubCategoria> listaSubCategorias = new ArrayList<>();
				while (rs.next()) {
					String id = rs.getString("id");
					String nome = rs.getString("nome");
					String icone = rs.getString("icone");

					SubCategoria subCategoria = SubCategoria.builder()
														.id(UUID.fromString(id))
														.nome(nome)
														.icone(icone).build();
					
					listaSubCategorias.add(subCategoria);
				}
				return listaSubCategorias;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<SubCategoria> pegarTodasSubCategoriasPorCategoriaId(String categoriaId) {
		String sql = "SELECT * FROM sub_categorias WHERE categoria_id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, categoriaId);
			try (ResultSet rs = ps.executeQuery()) {
				List<SubCategoria> listaSubCategorias = new ArrayList<>();
				while (rs.next()) {
					String id = rs.getString("id");
					String nome = rs.getString("nome");
					String icone = rs.getString("icone");

					SubCategoria subCategoria =
							SubCategoria.builder()
									.id(UUID.fromString(id))
									.nome(nome)
									.icone(icone).build();

					listaSubCategorias.add(subCategoria);
				}

				return listaSubCategorias;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
