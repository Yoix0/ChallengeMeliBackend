package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.PersonResponse;
import com.mercadolibre.challenge.application.dto.PersonListResponse;

/**
 * Puerto de entrada para consultar personas
 */
public interface GetPersonUseCase {

    /**
     * Obtiene una persona por su ID
     */
    PersonResponse getPersonById(Long id);

    /**
     * Obtiene una persona por su documento
     */
    PersonResponse getPersonByDocument(String documentType, String documentNumber);

    /**
     * Lista todas las personas con paginación
     */
    PersonListResponse getAllPersons(int offset, int limit);

    /**
     * Busca personas por nombre
     */
    PersonListResponse searchPersonsByName(String name, int offset, int limit);

    /**
     * Lista personas por estado
     */
    PersonListResponse getPersonsByStatus(String status, int offset, int limit);
}