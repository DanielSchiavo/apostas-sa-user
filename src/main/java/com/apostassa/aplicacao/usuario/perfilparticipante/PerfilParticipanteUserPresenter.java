package com.apostassa.aplicacao.usuario.perfilparticipante;

import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

public interface PerfilParticipanteUserPresenter {

    public String respostaCriarPerfilParticipante();

    String respostaAlterarPerfilParticipante(PerfilParticipante perfilParticipante);

    String respostaPegarDadosPerfilParticipante(PerfilParticipante perfilParticipante);
}
