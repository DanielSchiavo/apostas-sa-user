package com.apostassa.aplicacao.transacao.deposito;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.transacao.RepositorioDeTransacaoDeposito;
import com.apostassa.dominio.transacao.StatusTransacao;
import com.apostassa.dominio.transacao.TransacaoDeposito;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExecutarTransacaoDeposito {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeTransacaoDeposito repositorioDeTransacaoDeposito;

    private final TransacaoDepositoPresenter presenter;

    private final RepositorioDeUsuarioUser repositorioDeUsuarioUser;

    private final TransacaoDepositoMapper mapper;

    public ExecutarTransacaoDeposito(ProvedorConexao provedorConexao, RepositorioDeTransacaoDeposito repositorioDeTransacaoDeposito, TransacaoDepositoPresenter presenter, RepositorioDeUsuarioUser repositorioDeUsuarioUser) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeTransacaoDeposito = repositorioDeTransacaoDeposito;
        this.presenter = presenter;
        this.repositorioDeUsuarioUser = repositorioDeUsuarioUser;
        this.mapper = Mappers.getMapper(TransacaoDepositoMapper.class);
    }

    public String executa(ExecutarTransacaoDepositoDTO executarTransacaoDepositoDTO, String usuarioId) {
        try {
            BigDecimal saldo = repositorioDeUsuarioUser.pegarSaldoPorUsuarioId(usuarioId);

            TransacaoDeposito transacaoDeposito = mapper.formatarExecutarTransacaoDepositoDTOParaTransacaoDeposito(executarTransacaoDepositoDTO);
            transacaoDeposito.setUsuarioId(UUID.fromString(usuarioId));
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
