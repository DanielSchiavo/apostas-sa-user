package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
import com.apostassa.aplicacao.Token;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import org.mapstruct.factory.Mappers;


public class AutenticarUsuario {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	private CriptografiaSenha criptografiaSenha;
	
	private Token token;
	
	private UsuarioMapper usuarioMapper;
	
	public AutenticarUsuario(RepositorioDeUsuarioUser repositorioDeUsuario, CriptografiaSenha criptografiaSenha, Token token) {
		this.criptografiaSenha = criptografiaSenha;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.token = token;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(AutenticarUsuarioDTO autenticarUsuarioDTO) throws AutenticacaoException, ValidacaoException {
		Usuario usuario = usuarioMapper.formatarAutenticarUsuarioDTOParaUsuario(autenticarUsuarioDTO);
		
		try {
			Usuario usuarioPreAutenticado = repositorioDeUsuario.autenticarUsuario(usuario);
			
			criptografiaSenha.verificarSenha(usuarioPreAutenticado.getSenha(), autenticarUsuarioDTO.getSenha());
			
			String tokenUsuarioAutenticado = token.gerarToken(usuarioPreAutenticado);
			repositorioDeUsuario.commitarTransacao();
			return tokenUsuarioAutenticado;
		} catch (AutenticacaoException e) {
			e.printStackTrace();
			repositorioDeUsuario.rollbackTransacao();
			throw new AutenticacaoException(e.getMessage());
		} catch (ValidacaoException e) {
			e.printStackTrace();
			repositorioDeUsuario.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		}
	}
}
