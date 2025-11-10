package com.mercadolibre.challenge.domain.person.repository;

import com.mercadolibre.challenge.domain.person.Person;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de LECTURA (Query) de personas
 * Siguiendo el patrón CQRS: Command Query Responsibility Segregation
 *
 * Esta interfaz solo contiene operaciones que CONSULTAN datos sin modificar el estado
 * Beneficios:
 * - ISP (Interface Segregation Principle): Clientes solo dependen de lo que necesitan
 * - Optimización independiente: Se pueden optimizar lecturas (cache, réplicas) sin afectar escrituras
 * - Escalabilidad: Preparado para separar base de datos de lectura/escritura en el futuro
 */
public interface PersonQueryRepository {

    /**
     * Busca una persona por su ID
     * @param id ID de la persona
     * @return Optional con la persona si existe
     */
    Optional<Person> findById(Long id);

    /**
     * Busca una persona por tipo y número de documento
     * @param documentType Tipo de documento (CC, CE, TI, etc.)
     * @param documentNumber Número de documento
     * @return Optional con la persona si existe
     */
    Optional<Person> findByDocument(String documentType, String documentNumber);

    /**
     * Busca una persona por email
     * @param email Email de la persona
     * @return Optional con la persona si existe
     */
    Optional<Person> findByEmail(String email);

    /**
     * Lista todas las personas con paginación
     * @param offset Desde qué registro empezar
     * @param limit Cantidad máxima de registros a retornar
     * @return Lista de personas
     */
    List<Person> findAll(int offset, int limit);

    /**
     * Busca personas por estado con paginación
     * @param status Estado a filtrar (ACTIVE, INACTIVE, SUSPENDED)
     * @param offset Desde qué registro empezar
     * @param limit Cantidad máxima de registros a retornar
     * @return Lista de personas con el estado especificado
     */
    List<Person> findByStatus(String status, int offset, int limit);

    /**
     * Busca personas por nombre (busca en firstName y firstLastName)
     * @param name Texto a buscar en nombres
     * @param offset Desde qué registro empezar
     * @param limit Cantidad máxima de registros a retornar
     * @return Lista de personas que coinciden con el nombre
     */
    List<Person> findByName(String name, int offset, int limit);

    /**
     * Cuenta el total de personas en el sistema
     * @return Cantidad total de personas
     */
    long count();

    /**
     * Cuenta personas por estado
     * @param status Estado a contar
     * @return Cantidad de personas con ese estado
     */
    long countByStatus(String status);

    /**
     * Verifica si existe una persona con el documento especificado
     * @param documentType Tipo de documento
     * @param documentNumber Número de documento
     * @return true si existe, false si no
     */
    boolean existsByDocument(String documentType, String documentNumber);

    /**
     * Verifica si existe una persona con el email especificado
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);
}
