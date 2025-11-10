package com.mercadolibre.challenge.domain.common.exception.person;

import com.mercadolibre.challenge.domain.common.exception.BusinessException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción lanzada cuando se intenta crear una persona que ya existe
 */
public class PersonAlreadyExistsException extends BusinessException {

    public PersonAlreadyExistsException(String documentType, String documentNumber) {
        super(
            ExceptionCode.RESOURCE_CONFLICT,
            String.format("Ya existe una persona con documento %s-%s", documentType, documentNumber),
            "Ya existe una persona registrada con ese documento"
        );
    }

    public PersonAlreadyExistsException(String email) {
        super(
            ExceptionCode.RESOURCE_CONFLICT,
            String.format("Ya existe una persona con email %s", email),
            "Ya existe una persona registrada con ese email"
        );
    }
}