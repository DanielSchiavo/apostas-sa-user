package com.apostassa.aplicacao.usuario.validation;

public class RegrasComumDeValidacao {
	
	public static final String MESSAGE_PASSWORD_NOTNULL = "A senha não pode ser nula ou conter espaços";
	
	public static final String MESSAGE_MIN_PASSWORD_LENGTH = "A senha deve conter no minimo " + RegrasComumDeValidacao.MIN_PASSWORD_LENGTH + " digitos";
	
	public static final int MIN_PASSWORD_LENGTH = 10;
	
	public static final String MESSAGE_NOME_NOTNULL = "O nome não pode ser nulo ou conter espaços";
	
	public static final String MESSAGE_MIN_NOME_LENGTH = "O nome deve conter no mínimo " + RegrasComumDeValidacao.MIN_NOME_LENGTH + " letras";
	
	public static final int MIN_NOME_LENGTH = 2;
	
	public static final int IDADE_MINIMA = 18;

}
