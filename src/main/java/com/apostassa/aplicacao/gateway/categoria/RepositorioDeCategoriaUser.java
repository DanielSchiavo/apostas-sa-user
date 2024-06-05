package com.apostassa.aplicacao.gateway.categoria;


import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.categoria.Categoria;

import java.util.List;

public interface RepositorioDeCategoriaUser {
	
	public Categoria pegarCategoriaPorId(String categoriaId) throws ValidacaoException;

	public List<Categoria> pegarTodasCategorias(String ordenadoPor, String tipo);

}
