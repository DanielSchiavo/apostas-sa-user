package com.apostassa.aplicacao.gateway.transacao.deposito;

import com.apostassa.dominio.transacao.TransacaoDeposito;

public interface RepositorioDeTransacaoDeposito {

	void realizarTransacao(TransacaoDeposito transacao);
	
}
