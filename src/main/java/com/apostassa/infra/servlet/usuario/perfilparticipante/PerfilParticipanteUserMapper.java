package com.apostassa.infra.servlet.usuario.perfilparticipante;

import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "default", builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PerfilParticipanteUserMapper {

    abstract PerfilParticipante alterarPerfilParticipanteDtoParaPerfilParticipante(AlterarPerfilParticipanteDTO alterarPerfilParticipanteDTO);

}
