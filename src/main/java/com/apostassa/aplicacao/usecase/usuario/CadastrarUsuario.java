package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.infra.security.CriptografiaBCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class CadastrarUsuario {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	public CadastrarUsuario(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
	}

	public Map<String, String> executa(Usuario usuario) throws ValidacaoException {
		Set<ConstraintViolation<Usuario>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(usuario);
        for (ConstraintViolation<Usuario> violation : violations) {
        	throw new ValidacaoException(violation.getMessage());
        }

		String senhaCriptografada = CriptografiaBCrypt.criptografarSenha(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
		usuario.setDataCriacaoConta(LocalDateTime.now());
		usuario.setSaldo(BigDecimal.ZERO);
		
		try {
			repositorioDeUsuario.cadastrarUsuario(usuario);
			provedorConexao.commitarTransacao();
			return presenter.respostaCadastrarUsuario(usuario);
		} catch (ValidacaoException e) {
			e.printStackTrace();
			provedorConexao.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		} finally {
			provedorConexao.fecharConexao();
		}
	}
	
}
