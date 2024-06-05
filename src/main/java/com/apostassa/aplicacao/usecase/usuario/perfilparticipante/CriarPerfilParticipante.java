package com.apostassa.aplicacao.usecase.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.PerfilParticipanteUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

import java.time.LocalDateTime;

public class CriarPerfilParticipante {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser;

    private final PerfilParticipanteUserPresenter presenter;

    public CriarPerfilParticipante(ProvedorConexao provedorConexao, RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser, PerfilParticipanteUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorioDePerfilParticipanteUser = repositorioDePerfilParticipanteUser;
        this.presenter = presenter;
    }

    public String executa(String usuarioCpf) throws ValidacaoException {
        try {
            PerfilParticipante perfilParticipante = PerfilParticipante.builder()
                            .usuarioCpf(usuarioCpf).dataCriacao(LocalDateTime.now()).ativo(true).build();

            repositorioDePerfilParticipanteUser.criarPerfilParticipante(perfilParticipante);

            provedorConexao.commitarTransacao();

            return presenter.respostaCriarPerfilParticipante();
        } catch (ValidacaoException e) {
            e.printStackTrace();
            provedorConexao.rollbackTransacao();
            throw new ValidacaoException(e.getMessage());
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
