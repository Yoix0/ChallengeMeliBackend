package com.mercadolibre.challenge.domain.common.exception.payment_period;

import com.mercadolibre.challenge.domain.common.exception.BaseException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

import java.util.UUID;

/**
 * Excepción lanzada cuando no se encuentra un periodo de cobranza
 */
public class BaseNotFoundException extends BaseException {
    
    public BaseNotFoundException(UUID id) {
        super(ExceptionCode.RESOURCE_NOT_FOUND,
              "Periodo de cobranza no encontrado con ID: " + id,
              "El período de cobranza solicitado no existe");
    }
    
    public BaseNotFoundException(String message) {
        super(ExceptionCode.RESOURCE_NOT_FOUND, message);
    }
}