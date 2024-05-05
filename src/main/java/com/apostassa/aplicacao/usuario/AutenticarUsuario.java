package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.Token;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import org.mapstruct.factory.Mappers;

import java.util.Map;


public class AutenticarUsuario {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	private final CriptografiaSenha criptografiaSenha;
	
	private final Token token;
	
	private final UsuarioMapper usuarioMapper;
	
	public AutenticarUsuario(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter, CriptografiaSenha criptografiaSenha, Token token) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.criptografiaSenha = criptografiaSenha;
		this.token = token;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public Map<String, String> executa(AutenticarUsuarioDTO autenticarUsuarioDTO) throws AutenticacaoException, ValidacaoException {
		Usuario usuario = usuarioMapper.formatarAutenticarUsuarioDTOParaUsuario(autenticarUsuarioDTO);
		
		try {
			Usuario usuarioPreAutenticado = repositorioDeUsuario.autenticarUsuario(usuario);
			
			criptografiaSenha.verificarSenha(usuarioPreAutenticado.getSenha(), autenticarUsuarioDTO.getSenha());
			
			String tokenUsuarioAutenticado = token.gerarToken(usuarioPreAutenticado);
			provedorConexao.commitarTransacao();

			return presenter.respostaAutenticarUsuario(tokenUsuarioAutenticado);
		} catch (AutenticacaoException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new AutenticacaoException(e.getMessage());
		} catch (ValidacaoException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		} finally {
			provedorConexao.fecharConexao();
		}
	}
}
