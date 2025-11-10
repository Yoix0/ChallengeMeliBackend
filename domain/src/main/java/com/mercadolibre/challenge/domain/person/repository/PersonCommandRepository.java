package com.mercadolibre.challenge.domain.person.repository;

import com.mercadolibre.challenge.domain.person.Person;

/**
 * Puerto de salida para operaciones de ESCRITURA (Command) de personas
 * Siguiendo el patrón CQRS: Command Query Responsibility Segregation
 *
 * Esta interfaz solo contiene operaciones que MODIFICAN el estado del sistema
 */
public interface PersonCommandRepository {

    /**
     * Guarda una nueva persona en el sistema
     * @param person La persona a guardar
     * @return La persona guardada con su ID generado
     */
    Person save(Person person);

    /**
     * Actualiza una persona existente
     * @param person La persona con los datos actualizados
     * @return La persona actualizada
     */
    Person update(Person person);

    /**
     * Elimina una persona permanentemente del sistema
     * @param id ID de la persona a eliminar
     */
    void deleteById(Long id);
}