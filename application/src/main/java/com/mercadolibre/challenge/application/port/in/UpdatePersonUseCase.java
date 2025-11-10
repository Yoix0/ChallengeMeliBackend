package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.PersonRequest;
import com.mercadolibre.challenge.application.dto.PersonResponse;

/**
 * Puerto de entrada para actualizar personas
 */
public interface UpdatePersonUseCase {
    PersonResponse updatePerson(Long id, PersonRequest request);
}