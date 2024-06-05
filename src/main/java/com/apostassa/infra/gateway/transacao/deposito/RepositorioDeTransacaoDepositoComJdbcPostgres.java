package com.apostassa.infra.gateway.transacao.deposito;

import com.apostassa.aplicacao.gateway.transacao.deposito.RepositorioDeTransacaoDeposito;
import com.apostassa.dominio.transacao.TransacaoDeposito;

import java.sql.Connection;

public class RepositorioDeTransacaoDepositoComJdbcPostgres implements RepositorioDeTransacaoDeposito {

    private final Connection connection;

    public RepositorioDeTransacaoDepositoComJdbcPostgres(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void realizarTransacao(TransacaoDeposito transacao) {

    }
}
