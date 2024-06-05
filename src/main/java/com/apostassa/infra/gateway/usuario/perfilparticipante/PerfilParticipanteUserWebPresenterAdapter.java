package com.apostassa.infra.gateway.usuario.perfilparticipante;

import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.PerfilParticipanteUserPresenter;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import com.apostassa.infra.util.JacksonUtil;

public class PerfilParticipanteUserWebPresenterAdapter implements PerfilParticipanteUserPresenter {
    @Override
    public String respostaCriarPerfilParticipante() {
        return "Perfil participante criado com sucesso!";
    }

    @Override
    public String respostaAlterarPerfilParticipante(PerfilParticipante perfilParticipante) {
        return "Perfil participante alterado com sucesso!";
    }

    @Override
    public String respostaPegarDadosPerfilParticipante(PerfilParticipante perfilParticipante) {
        return JacksonUtil.serializador(perfilParticipante);
    }
}
