package com.apostassa.aplicacao.usecase.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.PerfilParticipanteUserPresenter;
import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import com.apostassa.infra.db.ProvedorConexaoJDBC;

public class PegarDadosPerfilParticipante {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDePerfilParticipanteUser repositorio;

    private final PerfilParticipanteUserPresenter presenter;

    public PegarDadosPerfilParticipante(ProvedorConexaoJDBC provedorConexao, RepositorioDePerfilParticipanteUser repositorio, PerfilParticipanteUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorio = repositorio;
        this.presenter = presenter;
    }

    public String executa(String usuarioCpf) throws ValidacaoException {
        try {
            PerfilParticipante perfilParticipante = repositorio.pegarDadosPerfilParticipante(usuarioCpf);

            return presenter.respostaPegarDadosPerfilParticipante(perfilParticipante);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
