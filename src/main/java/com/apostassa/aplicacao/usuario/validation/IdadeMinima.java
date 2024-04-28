package com.apostassa.aplicacao.usuario.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Past;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Past
@ReportAsSingleViolation
@Constraint(validatedBy = IdadeMinimaValidator.class)
public @interface IdadeMinima {
	
    String message() default "A idade mínima é {value} anos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}
