package com.mercadolibre.challenge.domain.common.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateArgument {

    public void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw ValidationException.nullField(fieldName);
        }
    }

    public void validateStringNotNullAndNotEmpty(String valor, String fieldName) {
        if (valor == null || valor.isEmpty()) {
            throw ValidationException.nullField(fieldName);
        }
    }

    public void validateLength(int number, int min, int max, String fieldName) {
        if (number < min || number > max) {
            throw ValidationException.invalidLength(fieldName,number,max);
        }
    }
}
