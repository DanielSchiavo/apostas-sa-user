package com.apostassa.infra.servlet.usuario.dto;

import com.apostassa.dominio.usuario.validation.RegrasComumDeValidacao;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutenticarUsuarioDTO {

	@NotBlank(message = RegrasComumDeValidacao.MESSAGE_EMAIL_NOTBLANK)
	private String email;
	
	@NotBlank(message = RegrasComumDeValidacao.MESSAGE_PASSWORD_NOTBLANK)
	private String senha;
	
}
