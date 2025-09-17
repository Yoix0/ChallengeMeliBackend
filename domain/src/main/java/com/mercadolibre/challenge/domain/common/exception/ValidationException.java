package com.mercadolibre.challenge.domain.common.exception;

import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción para errores de validación de datos
 */

public class ValidationException extends BaseException {
    
    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public ValidationException(String errorCode, String message, String userMessage) {
        super(errorCode, message, userMessage);
    }
    
    public static ValidationException nullField(String fieldName) {
        return new ValidationException(
            ExceptionCode.NULL_FIELD,
            "Campo nulo o vacio: " + fieldName,
            "El campo " + fieldName + " es obligatorio"
        );
    }
    
    public static ValidationException emptyField(String fieldName) {
        return new ValidationException(
            ExceptionCode.EMPTY_FIELD,
            "Campo vacío: " + fieldName,
            "El campo " + fieldName + " no puede estar vacío"
        );
    }
    
    public static ValidationException invalidValue(String fieldName, String value) {
        return new ValidationException(
            ExceptionCode.INVALID_VALUE,
            "Valor inválido para " + fieldName + ": " + value,
            "El valor proporcionado para " + fieldName + " no es válido"
        );
    }
    
    public static ValidationException invalidRange(String fieldName, String details) {
        return new ValidationException(
            ExceptionCode.INVALID_RANGE,
            "Valor fuera de rango para " + fieldName + ": " + details,
            "El valor de " + fieldName + " está fuera del rango permitido"
        );
    }
    
    public static ValidationException invalidLength(String fieldName, int current, int max) {
        return new ValidationException(
            ExceptionCode.INVALID_LENGTH,
            String.format("Longitud inválida para %s: %d (máximo %d)", fieldName, current, max),
            String.format("El campo %s excede la longitud máxima permitida", fieldName)
        );
    }
}