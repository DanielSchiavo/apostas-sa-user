package com.apostassa.aplicacao.usuario.perfilparticipante;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.usuario.perfilparticipante.AlterarPerfilParticipanteException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import com.apostassa.dominio.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

public class AlterarPerfilParticipante {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser;

    private final PerfilParticipanteUserPresenter presenter;

    private final PerfilParticipanteUserMapper perfilParticipanteMapper;

    public AlterarPerfilParticipante(ProvedorConexao provedorConexao, RepositorioDePerfilParticipanteUser repositorioDePerfilParticipanteUser, PerfilParticipanteUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorioDePerfilParticipanteUser = repositorioDePerfilParticipanteUser;
        this.presenter = presenter;
        this.perfilParticipanteMapper = Mappers.getMapper(PerfilParticipanteUserMapper.class);
    }

    public String executa(AlterarPerfilParticipanteDTO alterarPerfilParticipanteDTO, String usuarioId) throws AlterarPerfilParticipanteException {
        PerfilParticipante perfilParticipante = perfilParticipanteMapper.formatarUsuarioAlteraSeusDadosPessoaisDTOParaUsuario(alterarPerfilParticipanteDTO);
        perfilParticipante.setUsuarioId(UUID.fromString(usuarioId));

        try {
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
