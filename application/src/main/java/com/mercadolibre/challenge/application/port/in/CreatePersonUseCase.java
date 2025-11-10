package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.PersonRequest;
import com.mercadolibre.challenge.application.dto.PersonResponse;

/**
 * Puerto de entrada para crear personas
 */
public interface CreatePersonUseCase {
    PersonResponse createPerson(PersonRequest request);
}