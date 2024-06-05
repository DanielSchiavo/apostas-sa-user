package com.apostassa.aplicacao.gateway.usuario.perfilparticipante;

import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.AlterarPerfilParticipanteException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

public interface RepositorioDePerfilParticipanteUser {

    void criarPerfilParticipante(PerfilParticipante perfilParticipante) throws ValidacaoException;

    void alterarPerfilParticipante(PerfilParticipante perfilParticipante) throws AlterarPerfilParticipanteException;

    PerfilParticipante pegarDadosPerfilParticipante(String usuarioCpf) throws ValidacaoException;
}
