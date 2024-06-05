package com.apostassa.infra.servlet.transacao.apostaganha;

import com.apostassa.aplicacao.ConversoresComunsMapper;
import com.apostassa.aplicacao.usecase.transacao.apostaganha.ExecutarTransacaoApostaGanhaDTO;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "default", builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TransacaoApostaGanhaMapper implements ConversoresComunsMapper {

    @Mapping(source = "moedaId", target = "moedaId", qualifiedByName = "converterStringEmUUID")
    @Mapping(source = "apostaId", target = "apostaId", qualifiedByName = "converterStringEmUUID")
    public abstract TransacaoApostaGanha formatarExecutarTransacaoApostaGanhaDTOParaTransacaoApostaGanha(ExecutarTransacaoApostaGanhaDTO executarTransacaoApostaGanhaDTO);
}
