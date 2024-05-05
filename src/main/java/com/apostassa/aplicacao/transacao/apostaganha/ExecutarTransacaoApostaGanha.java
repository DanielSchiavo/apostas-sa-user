package com.apostassa.aplicacao.transacao.apostaganha;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.transacao.RepositorioDeTransacaoApostaGanha;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExecutarTransacaoApostaGanha {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoApostaGanha repositorioDeTransacaoApostaGanha;

    private final TransacaoApostaGanhaPresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    private final TransacaoApostaGanhaMapper transacaoApostaGanhaMapper;

    public ExecutarTransacaoApostaGanha(ProvedorConexao provedorConexao, RepositorioDeTransacaoApostaGanha repositorioDeTransacaoApostaGanha, TransacaoApostaGanhaPresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoApostaGanha = repositorioDeTransacaoApostaGanha;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
        this.transacaoApostaGanhaMapper = Mappers.getMapper(TransacaoApostaGanhaMapper.class);
    }

    public String executa(ExecutarTransacaoApostaGanhaDTO executarTransacaoApostaGanhaDTO, String usuarioId) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioId(usuarioId);

            TransacaoApostaGanha transacaoApostaGanha = transacaoApostaGanhaMapper.formatarExecutarTransacaoApostaGanhaDTOParaTransacaoApostaGanha(executarTransacaoApostaGanhaDTO);
            transacaoApostaGanha.setUsuarioId(UUID.fromString(usuarioId));
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
