package com.apostassa.infra.transacao.apostaganha;

import com.apostassa.aplicacao.transacao.apostaganha.TransacaoApostaGanhaPresenter;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;

public class TransacaoApostaGanhaWebAdapter implements TransacaoApostaGanhaPresenter {

    @Override
    public String respostaExecutarTransacaoApostaGanha(TransacaoApostaGanha transacaoAposta) {
        return "Transação aposta ganha realizada com sucesso!";
    }
}
