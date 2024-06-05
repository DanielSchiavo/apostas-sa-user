package com.apostassa.aplicacao.gateway.categoria.subcategoria;

import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.categoria.subcategoria.SubCategoria;

import java.util.List;

public interface RepositorioDeSubCategoriaUser {

	public SubCategoria pegarSubCategoriaPorId(String subCategoriaId) throws ValidacaoException;

	public List<SubCategoria> pegarTodasSubCategorias() throws ValidacaoException;

	public List<SubCategoria> pegarTodasSubCategoriasPorCategoriaId(String string);

}
