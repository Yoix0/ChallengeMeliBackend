package com.mercadolibre.challenge.application.port.in;

/**
 * Puerto de entrada para eliminar personas
 */
public interface DeletePersonUseCase {

    /**
     * Inactiva una persona (soft delete)
     */
    void deactivatePerson(Long id);

    /**
     * Activa una persona
     */
    void activatePerson(Long id);

    /**
     * Elimina permanentemente una persona (hard delete - solo admin)
     */
    void deletePerson(Long id);
}