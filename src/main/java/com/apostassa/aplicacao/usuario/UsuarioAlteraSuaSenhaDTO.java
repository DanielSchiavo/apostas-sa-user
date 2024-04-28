package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.usuario.validation.RegrasComumDeValidacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioAlteraSuaSenhaDTO {
	
	@NotNull(message = "A senha atual não pode ser nula")
	@Size(min = RegrasComumDeValidacao.MIN_PASSWORD_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_PASSWORD_LENGTH)
	private String senhaAtual;
	
	@NotNull(message = "A senha nova não pode ser nula")
	@Size(min = RegrasComumDeValidacao.MIN_PASSWORD_LENGTH, message = RegrasComumDeValidacao.MESSAGE_MIN_PASSWORD_LENGTH)
	private String senhaNova;
	
}
