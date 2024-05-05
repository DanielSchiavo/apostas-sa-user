package com.apostassa.infra.usuario.perfilparticipante;

import com.apostassa.aplicacao.usuario.perfilparticipante.PerfilParticipanteUserPresenter;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import com.apostassa.infra.util.JacksonUtil;

public class PerfilParticipanteUserWebAdapter implements PerfilParticipanteUserPresenter {
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
