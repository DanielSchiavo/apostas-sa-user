package com.apostassa.aplicacao.usecase.transacao.saque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExecutarTransacaoSaqueDTO {

    @NotBlank(message = "Você deve informar o ID da aposta")
    private String apostaId;

    @Positive(message = "O valor a ser sacado referente a aposta ganha deve ser positivo")
    private BigDecimal valor;

    @NotBlank(message = "Você deve informar o ID da moeda")
    private String moedaId;
}
