package com.mercadolibre.challenge.domain.common.exception;

import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción para errores de lógica de negocio
 */
public class BusinessException extends BaseException {
    
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public BusinessException(String errorCode, String message, String userMessage) {
        super(errorCode, message, userMessage);
    }
    
    public static BusinessException resourceConflict(String resourceName, String details) {
        return new BusinessException(
            ExceptionCode.RESOURCE_CONFLICT,
            "Conflicto con recurso " + resourceName + ": " + details,
            "La operación no se puede realizar debido a un conflicto con los datos existentes"
        );
    }
    
    public static BusinessException operationNotAllowed(String operation, String reason) {
        return new BusinessException(
            ExceptionCode.OPERATION_NOT_ALLOWED,
            "Operación no permitida '" + operation + "': " + reason,
            "No se puede realizar la operación solicitada en el estado actual"
        );
    }
    
    public static BusinessException resourceNotFound(String resourceName, String identifier) {
        return new BusinessException(
            ExceptionCode.RESOURCE_NOT_FOUND,
            resourceName + " no encontrado: " + identifier,
            "El recurso solicitado no existe"
        );
    }
}