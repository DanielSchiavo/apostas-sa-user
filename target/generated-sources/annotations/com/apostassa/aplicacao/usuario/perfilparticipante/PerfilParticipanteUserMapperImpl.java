package com.apostassa.aplicacao.usuario.perfilparticipante;

import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-03T01:07:04-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class PerfilParticipanteUserMapperImpl extends PerfilParticipanteUserMapper {

    @Override
    public PerfilParticipante formatarUsuarioAlteraSeusDadosPessoaisDTOParaUsuario(AlterarPerfilParticipanteDTO alterarPerfilParticipanteDTO) {
        if ( alterarPerfilParticipanteDTO == null ) {
            return null;
        }

        PerfilParticipante perfilParticipante = new PerfilParticipante();

        perfilParticipante.setFoto( alterarPerfilParticipanteDTO.getFoto() );
        perfilParticipante.setInstagram( alterarPerfilParticipanteDTO.getInstagram() );
        perfilParticipante.setFacebook( alterarPerfilParticipanteDTO.getFacebook() );
        perfilParticipante.setTwitter( alterarPerfilParticipanteDTO.getTwitter() );
        perfilParticipante.setFrase( alterarPerfilParticipanteDTO.getFrase() );
        perfilParticipante.setAtivo( alterarPerfilParticipanteDTO.getAtivo() );

        return perfilParticipante;
    }
}
