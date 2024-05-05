package com.apostassa.aplicacao.usuario.perfilparticipante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlterarPerfilParticipanteDTO {

    private String foto;

    private String instagram;

    private String facebook;

    private String twitter;

    private String frase;

    private Boolean ativo;
}
