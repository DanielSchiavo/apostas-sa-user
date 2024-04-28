package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.validation.IdadeMinima;
import com.apostassa.aplicacao.usuario.validation.RegrasComumDeValidacao;
import com.apostassa.dominio.usuario.CPF;
import com.apostassa.dominio.usuario.Celular;
import com.apostassa.dominio.usuario.Email;
import com.apostassa.dominio.usuario.RG;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAlteraSeusDadosPessoaisDTO {
	
	private CPF cpf;
	
	private RG rg;
	
	@NotBlank(message =  RegrasComumDeValidacao.MESSAGE_NOME_NOTNULL)
	@Size(min = RegrasComumDeValidacao.MIN_NOME_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_NOME_LENGTH)
	private String nome;
	
	private String sobrenome;
	
	private Email email;
	
	private Celular celular;
	
	@IdadeMinima(value = RegrasComumDeValidacao.IDADE_MINIMA)
	private LocalDate dataNascimento;

}
