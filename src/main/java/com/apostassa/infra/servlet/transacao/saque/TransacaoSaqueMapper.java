package com.apostassa.infra.servlet.transacao.saque;

import com.apostassa.aplicacao.ConversoresComunsMapper;
import com.apostassa.aplicacao.usecase.transacao.saque.ExecutarTransacaoSaqueDTO;
import com.apostassa.dominio.transacao.TransacaoSaque;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "default", builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TransacaoSaqueMapper implements ConversoresComunsMapper {

    @Mapping(source = "moedaId", target = "moedaId", qualifiedByName = "converterStringEmUUID")
    public abstract TransacaoSaque executarTransacaoSaqueDTOParaTransacaoSaque(ExecutarTransacaoSaqueDTO executarTransacaoSaqueDTO);
}
