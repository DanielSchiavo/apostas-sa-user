package com.apostassa.aplicacao.usecase.transacao.deposito;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.transacao.deposito.RepositorioDeTransacaoDeposito;
import com.apostassa.aplicacao.gateway.transacao.deposito.TransacaoDepositoPresenter;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoDeposito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExecutarTransacaoDeposito {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoDeposito repositorioDeTransacaoDeposito;

    private final TransacaoDepositoPresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    public ExecutarTransacaoDeposito(ProvedorConexao provedorConexao, RepositorioDeTransacaoDeposito repositorioDeTransacaoDeposito, TransacaoDepositoPresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoDeposito = repositorioDeTransacaoDeposito;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
    }

    public String executa(TransacaoDeposito transacaoDeposito, String usuarioCpf) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioCpf(usuarioCpf);

            transacaoDeposito.setUsuarioId(UUID.fromString(usuarioCpf));
            transacaoDeposito.setDataEHoraSolicitacao(LocalDateTime.now());
            transacaoDeposito.setStatus(StatusTransacao.PENDENTE);

            BigDecimal novoSaldo = transacaoDeposito.getValor().add(saldo);
            transacaoDeposito.setValor(novoSaldo);

            repositorioDeUsuarioUser.novoSaldo(novoSaldo);

            repositorioDeTransacaoDeposito.realizarTransacao(transacaoDeposito);
            provedorConexao.commitarTransacao();

            return presenter.respostaExecutarTransacaoDeposito(transacaoDeposito);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
