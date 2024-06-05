package com.apostassa.aplicacao.gateway.transacao.saque;

import com.apostassa.dominio.transacao.TransacaoSaque;

public interface RepositorioDeTransacaoSaque {

	void realizarTransacao(TransacaoSaque transacao);
	
}
