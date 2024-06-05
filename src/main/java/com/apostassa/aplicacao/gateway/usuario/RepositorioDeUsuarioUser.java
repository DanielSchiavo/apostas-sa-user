package com.apostassa.aplicacao.gateway.usuario;


import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.dominio.usuario.exceptions.UsuarioNaoEncontradoException;

import java.math.BigDecimal;

public interface RepositorioDeUsuarioUser {
	
	Usuario autenticarUsuario(String email) throws AutenticacaoException, ValidacaoException;
	
	
	
	void cadastrarUsuario(Usuario usuario) throws ValidacaoException;
	
	
	
	Usuario pegarDadosDoUsuarioPaginaInicial(String usuarioCpf) throws ValidacaoException;
	
	Usuario pegarDadosDoUsuarioPessoais(String usuarioCpf) throws ValidacaoException;

	BigDecimal pegarSaldoPorUsuarioCpf(String usuarioCpf);



	void usuarioAlteraSeusDadosPessoais(Usuario usuario) throws AlterarUsuarioException;

	void usuarioAlteraSuaSenha(String novaSenha, String email) throws AlterarUsuarioException;



	boolean verificarSeUsuarioJaConfirmouIdentidade(String usuarioCpf) throws UsuarioNaoEncontradoException;


	void novoSaldo(BigDecimal novoSaldo);
}
