package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.CriptografiaSenha;
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
import java.util.Set;
import java.util.UUID;

public class CadastrarUsuario {
	
	private RepositorioDeUsuarioUser repositorioDeUsuario;
	
	private CriptografiaSenha criptografiaSenha;
	
	private GeradorUUID geradorUuid;
	
	private UsuarioMapper usuarioMapper;
	
	public CadastrarUsuario(RepositorioDeUsuarioUser repositorioDeUsuario, CriptografiaSenha criptografiaSenha, GeradorUUID geradorUuid) {
		this.repositorioDeUsuario = repositorioDeUsuario;
		this.criptografiaSenha = criptografiaSenha;
		this.geradorUuid = geradorUuid;
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
	}

	public String executa(CadastrarUsuarioDTO cadastrarUsuarioDTO) throws ValidacaoException {
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
			repositorioDeUsuario.commitarTransacao();
		} catch (ValidacaoException e) {
			e.printStackTrace();
			repositorioDeUsuario.rollbackTransacao();
			throw new ValidacaoException(e.getMessage());
		}
		
		return "Cadastrado com sucesso!";
	}
	
}
