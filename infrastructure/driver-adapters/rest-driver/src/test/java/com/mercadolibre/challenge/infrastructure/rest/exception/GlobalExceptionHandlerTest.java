package com.mercadolibre.challenge.infrastructure.rest.exception;

import com.mercadolibre.challenge.domain.common.exception.BaseException;
import com.mercadolibre.challenge.domain.common.exception.BusinessException;
import com.mercadolibre.challenge.domain.common.exception.ValidationException;
import com.mercadolibre.challenge.domain.common.exception.payment_period.BaseNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for GlobalExceptionHandler")
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private WebRequest webRequest;


    @Test
    @DisplayName("Should handle ValidationException and return 400")
    void handleValidationException_ShouldReturn400() {
        // Arrange
        ValidationException exception = new ValidationException("VALIDATION_ERROR", "Error de validación", "Campo inválido");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleValidationException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle BusinessException and return 409")
    void handleBusinessException_ShouldReturn409() {
        // Arrange
        BusinessException exception = new BusinessException("BUSINESS_ERROR", "Error de negocio", "Operación no válida");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleBusinessException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle BaseNotFoundException and return 404")
    void handleNotFoundException_ShouldReturn404() {
        // Arrange
        BaseNotFoundException exception = new BaseNotFoundException("Recurso no encontrado");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleNotFoundException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }



    @Test
    @DisplayName("Should handle generic BaseException and return 500")
    void handleBaseException_ShouldReturn500() {
        // Arrange
        BaseException exception = new BaseException("GENERIC_ERROR", "Error genérico", "Mensaje técnico") {};

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleBaseException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException and return 400")
    void handleIllegalArgumentException_ShouldReturn400() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleIllegalArgumentException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle IllegalStateException and return 400")
    void handleIllegalStateException_ShouldReturn400() {
        // Arrange
        IllegalStateException exception = new IllegalStateException("Estado inválido");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleIllegalStateException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500")
    void handleGenericException_ShouldReturn500() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error inesperado");

        // Act
        ResponseEntity<?> response = globalExceptionHandler.handleGenericException(exception, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
    }
}