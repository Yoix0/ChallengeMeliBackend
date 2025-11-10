package com.mercadolibre.challenge.infrastructure.h2.person.repository;

import com.mercadolibre.challenge.infrastructure.h2.person.entity.PersonEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPersonRepository extends JpaRepository<PersonEntity, Long> {

    /**
     * Busca una persona por tipo y número de documento
     */
    Optional<PersonEntity> findByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    /**
     * Busca una persona por email
     */
    Optional<PersonEntity> findByEmail(String email);

    /**
     * Busca personas por estado
     */
    @Query("SELECT p FROM PersonEntity p WHERE p.status = :status")
    List<PersonEntity> findByStatus(@Param("status") String status, Pageable pageable);

    /**
     * Busca personas por nombre (firstName o firstLastName)
     */
    @Query("SELECT p FROM PersonEntity p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.firstLastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PersonEntity> findByNameContaining(@Param("name") String name, Pageable pageable);

    /**
     * Cuenta personas por estado
     */
    long countByStatus(String status);

    /**
     * Verifica si existe una persona con el documento dado
     */
    boolean existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    /**
     * Verifica si existe una persona con el email dado
     */
    boolean existsByEmail(String email);
}