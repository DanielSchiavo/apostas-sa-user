package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.validation.RegrasComumDeValidacao;
import com.apostassa.dominio.usuario.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutenticarUsuarioDTO {
	
	private Email email;
	
	@NotNull(message = RegrasComumDeValidacao.MESSAGE_PASSWORD_NOTNULL)
	@Size(min = RegrasComumDeValidacao.MIN_PASSWORD_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_PASSWORD_LENGTH)
	private String senha;
	
}
