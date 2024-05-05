package com.apostassa.infra.transacao.apostaganha;

import com.apostassa.dominio.transacao.RepositorioDeTransacaoApostaGanha;
import com.apostassa.dominio.transacao.RepositorioDeTransacaoDeposito;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;
import com.apostassa.dominio.transacao.TransacaoDeposito;

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
