package com.mercadolibre.challenge.infrastructure.rest.response;

import java.util.List;
import java.util.Map;

/**
 * Respuesta específica para errores de validación
 */
public class ValidationErrorResponse extends ApiResponse<Void> {
    
    private Map<String, List<String>> fieldErrors;
    
    private ValidationErrorResponse() {
        super();
    }
    
    public static ValidationErrorResponse create(String errorCode, String message, Map<String, List<String>> fieldErrors) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setSuccess(false) ;
        response.setErrorCode(errorCode);
        response.setMessage(message);
        response.fieldErrors = fieldErrors;
        return response;
    }
    
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}