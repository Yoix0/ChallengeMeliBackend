package com.mercadolibre.challenge.domain.common.exception.person;

import com.mercadolibre.challenge.domain.common.exception.BusinessException;
import com.mercadolibre.challenge.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción lanzada cuando no se encuentra una persona
 */
public class PersonNotFoundException extends BusinessException {

    public PersonNotFoundException(Long personId) {
        super(
            ExceptionCode.RESOURCE_NOT_FOUND,
            String.format("Persona con ID %d no encontrada", personId),
            "La persona solicitada no existe en el sistema"
        );
    }

    public PersonNotFoundException(String documentType, String documentNumber) {
        super(
            ExceptionCode.RESOURCE_NOT_FOUND,
            String.format("Persona con documento %s-%s no encontrada", documentType, documentNumber),
            "La persona con el documento especificado no existe en el sistema"
        );
    }
}