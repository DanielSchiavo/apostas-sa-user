package com.apostassa.aplicacao.transacao.deposito;

import com.apostassa.dominio.transacao.TransacaoDeposito;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-03T01:07:02-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class TransacaoDepositoMapperImpl extends TransacaoDepositoMapper {

    @Override
    public TransacaoDeposito formatarExecutarTransacaoDepositoDTOParaTransacaoDeposito(ExecutarTransacaoDepositoDTO executarTransacaoDepositoDTO) {
        if ( executarTransacaoDepositoDTO == null ) {
            return null;
        }

        TransacaoDeposito transacaoDeposito = new TransacaoDeposito();

        transacaoDeposito.setMoedaId( converterStringEmUUID( executarTransacaoDepositoDTO.getMoedaId() ) );
        transacaoDeposito.setValor( executarTransacaoDepositoDTO.getValor() );

        return transacaoDeposito;
    }
}
