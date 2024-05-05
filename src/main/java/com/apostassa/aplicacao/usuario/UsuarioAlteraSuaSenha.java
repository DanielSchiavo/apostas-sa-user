package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
import com.apostassa.aplicacao.ProvedorConexao;
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

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	private final CriptografiaSenha criptografiaSenha;
	
	private final UsuarioMapper usuarioMapper;
	
	public UsuarioAlteraSuaSenha(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter, CriptografiaSenha criptografiaSenha) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.criptografiaSenha = criptografiaSenha;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(UsuarioAlteraSuaSenhaDTO usuarioAlteraSuaSenhaDTO, String email) throws AutenticacaoException, AlterarUsuarioException, ValidacaoException {
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
			provedorConexao.commitarTransacao();

			return presenter.respostaUsuarioAlteraSuaSenha();
		} catch (AlterarUsuarioException e) {
			provedorConexao.rollbackTransacao();
			e.printStackTrace();
			throw new AlterarUsuarioException(e.getMessage());
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
