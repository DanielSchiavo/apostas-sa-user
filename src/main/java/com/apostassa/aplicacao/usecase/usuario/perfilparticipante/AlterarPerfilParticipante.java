package com.apostassa.aplicacao.usecase.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.PerfilParticipanteUserPresenter;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;
import com.apostassa.dominio.usuario.perfilparticipante.AlterarPerfilParticipanteException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

public class AlterarPerfilParticipante {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser;

    private final PerfilParticipanteUserPresenter presenter;

    public AlterarPerfilParticipante(ProvedorConexao provedorConexao, RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser, PerfilParticipanteUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorioDePerfilParticipanteUser = repositorioDePerfilParticipanteUser;
        this.presenter = presenter;
    }

    public String executa(PerfilParticipante perfilParticipante, String usuarioCpf) throws AlterarPerfilParticipanteException {
        try {
            perfilParticipante.setUsuarioCpf(usuarioCpf);

            repositorioDePerfilParticipanteUser.alterarPerfilParticipante(perfilParticipante);
            provedorConexao.commitarTransacao();

            return presenter.respostaAlterarPerfilParticipante(perfilParticipante);
        } catch (AlterarPerfilParticipanteException e) {
            e.printStackTrace();
            provedorConexao.rollbackTransacao();
            throw new AlterarPerfilParticipanteException(e.getMessage());
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
