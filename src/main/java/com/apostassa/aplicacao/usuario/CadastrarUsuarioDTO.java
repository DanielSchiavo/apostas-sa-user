package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.validation.IdadeMinima;
import com.apostassa.aplicacao.usuario.validation.RegrasComumDeValidacao;
import com.apostassa.dominio.usuario.CPF;
import com.apostassa.dominio.usuario.Celular;
import com.apostassa.dominio.usuario.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CadastrarUsuarioDTO {
	
	private UUID id;
	
	private CPF cpf;
	
	@NotBlank(message =  RegrasComumDeValidacao.MESSAGE_NOME_NOTNULL)
	@Size(min = RegrasComumDeValidacao.MIN_NOME_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_NOME_LENGTH)
	private String nome;
	
	private String sobrenome;
	
	private Email email;
	
	private Celular celular;
	
	@NotNull(message = RegrasComumDeValidacao.MESSAGE_PASSWORD_NOTNULL)
	@Size(min = RegrasComumDeValidacao.MIN_PASSWORD_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_PASSWORD_LENGTH)
	private String senha;
	
	@IdadeMinima(value = RegrasComumDeValidacao.IDADE_MINIMA)
	private LocalDate dataNascimento;
	
	private LocalDateTime dataCriacaoConta;
	
	private BigDecimal saldo;
	
	
}
