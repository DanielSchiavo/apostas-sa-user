package com.apostassa.aplicacao.transacao.saque;

import com.apostassa.dominio.transacao.TransacaoSaque;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-03T01:07:02-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class TransacaoSaqueMapperImpl extends TransacaoSaqueMapper {

    @Override
    public TransacaoSaque formatarExecutarTransacaoSaqueDTOParaTransacaoSaque(ExecutarTransacaoSaqueDTO executarTransacaoSaqueDTO) {
        if ( executarTransacaoSaqueDTO == null ) {
            return null;
        }

        TransacaoSaque transacaoSaque = new TransacaoSaque();

        transacaoSaque.setMoedaId( converterStringEmUUID( executarTransacaoSaqueDTO.getMoedaId() ) );
        transacaoSaque.setValor( executarTransacaoSaqueDTO.getValor() );

        return transacaoSaque;
    }
}
