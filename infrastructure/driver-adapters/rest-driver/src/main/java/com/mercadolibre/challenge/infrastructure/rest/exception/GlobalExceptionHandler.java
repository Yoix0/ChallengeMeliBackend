package com.mercadolibre.challenge.infrastructure.rest.exception;

import com.mercadolibre.challenge.domain.common.exception.BaseException;
import com.mercadolibre.challenge.domain.common.exception.ValidationException;
import com.mercadolibre.challenge.domain.common.exception.BusinessException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;
import com.mercadolibre.challenge.domain.common.exception.payment_period.BaseNotFoundException;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import com.mercadolibre.challenge.infrastructure.rest.response.ValidationErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST
 */
@RestControllerAdvice
@Component
public class GlobalExceptionHandler {

    private <T> ResponseEntity<T> json(HttpStatus status, T body) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                              WebRequest request) {
        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            fieldErrors.computeIfAbsent(field, k -> new ArrayList<>()).add(error.getDefaultMessage());
        });

        ValidationErrorResponse response = ValidationErrorResponse.create(
                ExceptionCode.VALIDATION_ERROR, "Error de validación en los datos", fieldErrors
        );
        return json(HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex, WebRequest r) {
        return json(HttpStatus.BAD_REQUEST, ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex, WebRequest r) {
        return json(HttpStatus.CONFLICT, ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BaseNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(BaseNotFoundException ex, WebRequest r) {
        return json(HttpStatus.NOT_FOUND, ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex, WebRequest r) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest r) {
        return json(HttpStatus.BAD_REQUEST, ApiResponse.error("INVALID_ARGUMENT", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex, WebRequest r) {
        return json(HttpStatus.BAD_REQUEST, ApiResponse.error("INVALID_STATE", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, WebRequest r) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponse.error("INTERNAL_SERVER_ERROR", "Ha ocurrido un error interno del servidor"));
    }
}