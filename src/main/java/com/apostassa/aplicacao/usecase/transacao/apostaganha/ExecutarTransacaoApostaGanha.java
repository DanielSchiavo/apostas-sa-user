package com.apostassa.aplicacao.usecase.transacao.apostaganha;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.transacao.apostaganha.RepositorioDeTransacaoApostaGanha;
import com.apostassa.aplicacao.gateway.transacao.apostaganha.TransacaoApostaGanhaPresenter;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExecutarTransacaoApostaGanha {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoApostaGanha repositorioDeTransacaoApostaGanha;

    private final TransacaoApostaGanhaPresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    public ExecutarTransacaoApostaGanha(ProvedorConexao provedorConexao, RepositorioDeTransacaoApostaGanha repositorioDeTransacaoApostaGanha, TransacaoApostaGanhaPresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoApostaGanha = repositorioDeTransacaoApostaGanha;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
    }

    public String executa(TransacaoApostaGanha transacaoApostaGanha, String usuarioCpf) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioCpf(usuarioCpf);

            transacaoApostaGanha.setUsuarioId(UUID.fromString(usuarioCpf));
            transacaoApostaGanha.setDataEHoraSolicitacao(LocalDateTime.now());
            transacaoApostaGanha.setStatus(StatusTransacao.PENDENTE);

            BigDecimal novoSaldo = transacaoApostaGanha.getValor().add(saldo);
            transacaoApostaGanha.setValor(novoSaldo);

            repositorioDeUsuarioUser.novoSaldo(novoSaldo);

            repositorioDeTransacaoApostaGanha.realizarTransacao(transacaoApostaGanha);
            provedorConexao.commitarTransacao();

            return presenter.respostaExecutarTransacaoApostaGanha(transacaoApostaGanha);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
