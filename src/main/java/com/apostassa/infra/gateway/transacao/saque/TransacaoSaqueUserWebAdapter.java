package com.apostassa.infra.gateway.transacao.saque;

import com.apostassa.aplicacao.gateway.transacao.saque.TransacaoSaquePresenter;
import com.apostassa.dominio.transacao.TransacaoSaque;

public class TransacaoSaqueUserWebAdapter implements TransacaoSaquePresenter {

    @Override
    public String respostaExecutarTransacaoSaque(TransacaoSaque transacaoSaque) {
        return "Transação de saque realizado com sucesso!";
    }
}
