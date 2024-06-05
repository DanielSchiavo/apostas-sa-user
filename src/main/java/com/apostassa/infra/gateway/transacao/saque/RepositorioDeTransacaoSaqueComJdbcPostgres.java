package com.apostassa.infra.gateway.transacao.saque;

import com.apostassa.aplicacao.gateway.transacao.saque.RepositorioDeTransacaoSaque;
import com.apostassa.dominio.transacao.TransacaoSaque;

import java.sql.Connection;

public class RepositorioDeTransacaoSaqueComJdbcPostgres implements RepositorioDeTransacaoSaque {

    private final Connection connection;

    public RepositorioDeTransacaoSaqueComJdbcPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void realizarTransacao(TransacaoSaque transacao) {

    }
}
