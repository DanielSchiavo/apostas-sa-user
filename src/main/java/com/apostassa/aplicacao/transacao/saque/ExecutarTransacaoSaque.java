package com.apostassa.aplicacao.transacao.saque;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.transacao.RepositorioDeTransacaoSaque;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoSaque;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExecutarTransacaoSaque {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoSaque repositorioDeTransacaoSaque;

    private final TransacaoSaquePresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    private final TransacaoSaqueMapper mapper;

    public ExecutarTransacaoSaque(ProvedorConexao provedorConexao, RepositorioDeTransacaoSaque repositorioDeTransacaoSaque, TransacaoSaquePresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoSaque = repositorioDeTransacaoSaque;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
        this.mapper = Mappers.getMapper(TransacaoSaqueMapper.class);
    }

    public String executa(ExecutarTransacaoSaqueDTO executarTransacaoSaqueDTO, String usuarioId) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioId(usuarioId);

            TransacaoSaque transacaoSaque = mapper.formatarExecutarTransacaoSaqueDTOParaTransacaoSaque(executarTransacaoSaqueDTO);
            transacaoSaque.setUsuarioId(UUID.fromString(usuarioId));
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
