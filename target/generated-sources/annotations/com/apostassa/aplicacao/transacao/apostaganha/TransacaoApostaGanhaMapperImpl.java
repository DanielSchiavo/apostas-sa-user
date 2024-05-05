package com.apostassa.aplicacao.transacao.apostaganha;

import com.apostassa.dominio.transacao.TransacaoApostaGanha;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-03T01:07:02-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class TransacaoApostaGanhaMapperImpl extends TransacaoApostaGanhaMapper {

    @Override
    public TransacaoApostaGanha formatarExecutarTransacaoApostaGanhaDTOParaTransacaoApostaGanha(ExecutarTransacaoApostaGanhaDTO executarTransacaoApostaGanhaDTO) {
        if ( executarTransacaoApostaGanhaDTO == null ) {
            return null;
        }

        TransacaoApostaGanha transacaoApostaGanha = new TransacaoApostaGanha();

        transacaoApostaGanha.setMoedaId( converterStringEmUUID( executarTransacaoApostaGanhaDTO.getMoedaId() ) );
        transacaoApostaGanha.setApostaId( converterStringEmUUID( executarTransacaoApostaGanhaDTO.getApostaId() ) );
        transacaoApostaGanha.setValor( executarTransacaoApostaGanhaDTO.getValor() );

        return transacaoApostaGanha;
    }
}
