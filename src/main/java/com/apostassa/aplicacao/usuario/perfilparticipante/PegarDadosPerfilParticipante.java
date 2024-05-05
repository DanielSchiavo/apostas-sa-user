package com.apostassa.aplicacao.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.usuario.perfilparticipante.RepositorioDePerfilParticipanteUserComJdbcPostgres;

public class PegarDadosPerfilParticipante {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDePerfilParticipanteUserComJdbcPostgres repositorio;

    private final PerfilParticipanteUserPresenter presenter;

    public PegarDadosPerfilParticipante(ProvedorConexaoJDBC provedorConexao, RepositorioDePerfilParticipanteUserComJdbcPostgres repositorio, PerfilParticipanteUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorio = repositorio;
        this.presenter = presenter;
    }

    public String executa(String usuarioId) throws ValidacaoException {
        try {
            PerfilParticipante perfilParticipante = repositorio.pegarDadosPerfilParticipante(usuarioId);

            return presenter.respostaPegarDadosPerfilParticipante(perfilParticipante);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}
