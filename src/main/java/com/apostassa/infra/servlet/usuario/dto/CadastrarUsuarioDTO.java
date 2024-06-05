package com.apostassa.infra.servlet.usuario.dto;

import com.apostassa.dominio.usuario.Celular;
import com.apostassa.dominio.usuario.validation.RegrasComumDeValidacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CadastrarUsuarioDTO {

	@NotBlank(message = RegrasComumDeValidacao.MESSAGE_CPF_NOTBLANK)
	private String cpf;
	
	@NotBlank(message =  RegrasComumDeValidacao.MESSAGE_NOME_NOTBLANK)
	private String nome;

	private String sobrenome;

	@NotBlank(message =  RegrasComumDeValidacao.MESSAGE_EMAIL_NOTBLANK)
	private String email;
	
	private Celular celular;
	
	@NotBlank(message = RegrasComumDeValidacao.MESSAGE_PASSWORD_NOTBLANK)
	private String senha;
	
	@NotNull(message = RegrasComumDeValidacao.MESSAGE_DATANASCIMENTO_NOTNULL)
	private LocalDate dataNascimento;

}
