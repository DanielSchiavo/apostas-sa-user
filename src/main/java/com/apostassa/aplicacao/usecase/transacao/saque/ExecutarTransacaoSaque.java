package com.apostassa.aplicacao.usecase.transacao.saque;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.transacao.saque.RepositorioDeTransacaoSaque;
import com.apostassa.aplicacao.gateway.transacao.saque.TransacaoSaquePresenter;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoSaque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExecutarTransacaoSaque {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoSaque repositorioDeTransacaoSaque;

    private final TransacaoSaquePresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    public ExecutarTransacaoSaque(ProvedorConexao provedorConexao, RepositorioDeTransacaoSaque repositorioDeTransacaoSaque, TransacaoSaquePresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoSaque = repositorioDeTransacaoSaque;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
    }

    public String executa(TransacaoSaque transacaoSaque, String usuarioCpf) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioCpf(usuarioCpf);

            transacaoSaque.setUsuarioCpf(usuarioCpf);
            transacaoSaque.setDataEHoraSolicitacao(LocalDateTime.now());
            transacaoSaque.setStatus(StatusTransacao.PENDENTE);

            BigDecimal novoSaldo = transacaoSaque.getValor().add(saldo);
            transacaoSaque.setValor(novoSaldo);

            repositorioDeUsuarioUser.novoSaldo(novoSaldo);

            repositorioDeTransacaoSaque.realizarTransacao(transacaoSaque);
            provedorConexao.commitarTransacao();

            return presenter.respostaExecutarTransacaoSaque(transacaoSaque);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
