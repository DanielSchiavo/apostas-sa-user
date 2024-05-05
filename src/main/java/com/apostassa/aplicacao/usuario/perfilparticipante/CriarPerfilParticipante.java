package com.apostassa.aplicacao.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;

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

    public String executa(String usuarioId) throws ValidacaoException {
        try {
            LocalDateTime dataCriacao = LocalDateTime.now();
            boolean ativo = true;

            repositorioDePerfilParticipanteUser.criarPerfilParticipante(usuarioId, dataCriacao, ativo);

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
