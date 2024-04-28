package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Email;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.mapstruct.factory.Mappers;

import java.util.Set;

public class UsuarioAlteraSuaSenha {

	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	private CriptografiaSenha criptografiaSenha;
	
	private UsuarioMapper usuarioMapper;
	
	public UsuarioAlteraSuaSenha(RepositorioDeUsuarioUser repositorioDeUsuario, CriptografiaSenha criptografiaSenha) {
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.criptografiaSenha = criptografiaSenha;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public void executa(UsuarioAlteraSuaSenhaDTO usuarioAlteraSuaSenhaDTO, String email) throws AutenticacaoException, AlterarUsuarioException, ValidacaoException {
		Set<ConstraintViolation<UsuarioAlteraSuaSenhaDTO>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(usuarioAlteraSuaSenhaDTO);
        for (ConstraintViolation<UsuarioAlteraSuaSenhaDTO> violation : violations) {
        	throw new ValidacaoException(violation.getMessage());
        }
		
		Usuario usuario = usuarioMapper.formatarUsuarioAlteraSuaSenhaDTOParaUsuario(usuarioAlteraSuaSenhaDTO);
		usuario.setEmail(new Email(email));
		try {
			Usuario usuarioAutenticado = repositorioDeUsuario.autenticarUsuario(usuario);
			
			criptografiaSenha.verificarSenha(usuarioAutenticado.getSenha(), usuarioAlteraSuaSenhaDTO.getSenhaAtual());
			String senhaNovaCriptografada = criptografiaSenha.criptografarSenha(usuarioAlteraSuaSenhaDTO.getSenhaNova());
			usuario.setSenha(senhaNovaCriptografada);
			repositorioDeUsuario.usuarioAlteraSuaSenha(usuario);
			repositorioDeUsuario.commitarTransacao();
		} catch (AlterarUsuarioException e) {
			repositorioDeUsuario.rollbackTransacao();
			e.printStackTrace();
			throw new AlterarUsuarioException(e.getMessage());
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
