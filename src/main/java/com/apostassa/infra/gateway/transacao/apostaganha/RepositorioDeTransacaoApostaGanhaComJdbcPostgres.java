package com.apostassa.infra.gateway.transacao.apostaganha;

import com.apostassa.aplicacao.gateway.transacao.apostaganha.RepositorioDeTransacaoApostaGanha;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;

import java.sql.Connection;

public class RepositorioDeTransacaoApostaGanhaComJdbcPostgres implements RepositorioDeTransacaoApostaGanha {

    private final Connection connection;

    public RepositorioDeTransacaoApostaGanhaComJdbcPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void realizarTransacao(TransacaoApostaGanha transacao) {

    }
}
