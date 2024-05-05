package com.apostassa.infra.transacao.deposito;

import com.apostassa.aplicacao.transacao.deposito.TransacaoDepositoPresenter;
import com.apostassa.dominio.transacao.TransacaoDeposito;

public class TransacaoDepositoUserWebAdapter implements TransacaoDepositoPresenter {

    @Override
    public String respostaExecutarTransacaoDeposito(TransacaoDeposito transacaoAposta) {
        return "Transação de deposito realizada com sucesso!";
    }
}
