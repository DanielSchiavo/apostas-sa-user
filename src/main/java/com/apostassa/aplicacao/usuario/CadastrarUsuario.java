package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.usuario.mapper.UsuarioMapper;
import com.apostassa.dominio.GeradorUUID;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CadastrarUsuario {

	private final ProvedorConexao provedorConexao;

	private final RepositorioDeUsuarioUser repositorioDeUsuario;

	private final UsuarioUserPresenter presenter;
	
	private final CriptografiaSenha criptografiaSenha;
	
	private final GeradorUUID geradorUuid;
	
	private final UsuarioMapper usuarioMapper;
	
	public CadastrarUsuario(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter, CriptografiaSenha criptografiaSenha, GeradorUUID geradorUuid) {
		this.provedorConexao = provedorConexao;
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.presenter = presenter;
		this.criptografiaSenha = criptografiaSenha;
		this.geradorUuid = geradorUuid;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public Map<String, String> executa(CadastrarUsuarioDTO cadastrarUsuarioDTO) throws ValidacaoException {
		Set<ConstraintViolation<CadastrarUsuarioDTO>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(cadastrarUsuarioDTO);
        for (ConstraintViolation<CadastrarUsuarioDTO> violation : violations) {
        	throw new ValidacaoException(violation.getMessage());
        }
		
		String uuidString = geradorUuid.gerarUUID().toString();
		cadastrarUsuarioDTO.setId(UUID.fromString(uuidString));
		
		String senhaCriptografada = criptografiaSenha.criptografarSenha(cadastrarUsuarioDTO.getSenha());
		cadastrarUsuarioDTO.setSenha(senhaCriptografada);
		
		cadastrarUsuarioDTO.setDataCriacaoConta(LocalDateTime.now());
		cadastrarUsuarioDTO.setSaldo(BigDecimal.ZERO);
		
		Usuario usuario = usuarioMapper.formatarCadastrarUsuarioDTOParaUsuario(cadastrarUsuarioDTO);
		
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
