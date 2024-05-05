package com.apostassa.aplicacao.transacao.deposito;

import com.apostassa.aplicacao.ConversoresComunsMapper;
import com.apostassa.dominio.transacao.TransacaoDeposito;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "default", builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TransacaoDepositoMapper implements ConversoresComunsMapper {

    @Mapping(source = "moedaId", target = "moedaId", qualifiedByName = "converterStringEmUUID")
    public abstract TransacaoDeposito formatarExecutarTransacaoDepositoDTOParaTransacaoDeposito(ExecutarTransacaoDepositoDTO executarTransacaoDepositoDTO);
}
