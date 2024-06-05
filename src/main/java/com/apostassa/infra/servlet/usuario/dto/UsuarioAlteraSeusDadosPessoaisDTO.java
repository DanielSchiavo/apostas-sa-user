package com.apostassa.infra.servlet.usuario.dto;

import com.apostassa.dominio.usuario.Celular;
import com.apostassa.dominio.usuario.RG;
import com.apostassa.infra.util.validation.PeloMenosUmNaoNulo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PeloMenosUmNaoNulo(fieldNames = {"*"})
public class UsuarioAlteraSeusDadosPessoaisDTO {

	private String cpf;

	private RG rg;
	
	private String nome;
	
	private String sobrenome;

	private String email;

	private Celular celular;
	
	private LocalDate dataNascimento;

}
