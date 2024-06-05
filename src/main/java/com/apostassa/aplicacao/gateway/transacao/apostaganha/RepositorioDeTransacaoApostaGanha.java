package com.apostassa.aplicacao.gateway.transacao.apostaganha;

import com.apostassa.dominio.transacao.TransacaoApostaGanha;

public interface RepositorioDeTransacaoApostaGanha {

	void realizarTransacao(TransacaoApostaGanha transacao);
	
}
