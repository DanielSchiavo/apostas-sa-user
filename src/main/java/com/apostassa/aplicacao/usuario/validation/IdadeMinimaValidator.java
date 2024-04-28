package com.apostassa.aplicacao.usuario.validation;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdadeMinimaValidator implements ConstraintValidator<IdadeMinima, LocalDate> {

    private int value;

    @Override
    public void initialize(IdadeMinima constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(dateOfBirth, today);
        int age = period.getYears();

        return age >= value;
    }
}