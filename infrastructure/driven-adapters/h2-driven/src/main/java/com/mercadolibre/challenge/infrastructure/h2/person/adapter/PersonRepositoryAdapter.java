package com.mercadolibre.challenge.infrastructure.h2.person.adapter;

import com.mercadolibre.challenge.domain.person.Person;
import com.mercadolibre.challenge.domain.person.PersonStatus;
import com.mercadolibre.challenge.domain.person.repository.PersonCommandRepository;
import com.mercadolibre.challenge.domain.person.repository.PersonQueryRepository;
import com.mercadolibre.challenge.infrastructure.h2.person.entity.PersonEntity;
import com.mercadolibre.challenge.infrastructure.h2.person.repository.JpaPersonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa los puertos PersonCommandRepository y PersonQueryRepository usando JPA
 * Implementa CQRS (Command Query Responsibility Segregation) a nivel de interfaces
 *
 * Esta clase única implementa ambos contratos porque:
 * 1. En este contexto, ambas operaciones usan la misma fuente de datos (H2)
 * 2. Spring puede inyectar esta clase donde se necesite PersonCommandRepository o PersonQueryRepository
 * 3. Permite migrar fácilmente a bases de datos separadas en el futuro
 */
@Repository
public class PersonRepositoryAdapter implements PersonCommandRepository, PersonQueryRepository {

    private final JpaPersonRepository jpaPersonRepository;

    public PersonRepositoryAdapter(JpaPersonRepository jpaPersonRepository) {
        this.jpaPersonRepository = jpaPersonRepository;
    }

    @Override
    public Person save(Person person) {
        PersonEntity entity = domainToEntity(person);
        PersonEntity savedEntity = jpaPersonRepository.save(entity);
        return entityToDomain(savedEntity);
    }

    @Override
    public Person update(Person person) {
        PersonEntity entity = domainToEntity(person);
        PersonEntity updatedEntity = jpaPersonRepository.save(entity);
        return entityToDomain(updatedEntity);
    }

    @Override
    public Optional<Person> findById(Long id) {
        return jpaPersonRepository.findById(id)
            .map(this::entityToDomain);
    }

    @Override
    public Optional<Person> findByDocument(String documentType, String documentNumber) {
        return jpaPersonRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber)
            .map(this::entityToDomain);
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return jpaPersonRepository.findByEmail(email)
            .map(this::entityToDomain);
    }

    @Override
    public List<Person> findAll(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaPersonRepository.findAll(pageable).stream()
            .map(this::entityToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByStatus(String status, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaPersonRepository.findByStatus(status, pageable).stream()
            .map(this::entityToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Person> findByName(String name, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaPersonRepository.findByNameContaining(name, pageable).stream()
            .map(this::entityToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaPersonRepository.count();
    }

    @Override
    public long countByStatus(String status) {
        return jpaPersonRepository.countByStatus(status);
    }

    @Override
    public boolean existsByDocument(String documentType, String documentNumber) {
        return jpaPersonRepository.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaPersonRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaPersonRepository.deleteById(id);
    }

    // ==================== MAPPERS ====================

    private PersonEntity domainToEntity(Person person) {
        return PersonEntity.builder()
            .id(person.getId())
            .documentType(person.getDocumentType())
            .documentNumber(person.getDocumentNumber())
            .firstName(person.getFirstName())
            .secondName(person.getSecondName())
            .firstLastName(person.getFirstLastName())
            .secondLastName(person.getSecondLastName())
            .birthDate(person.getBirthDate())
            .email(person.getEmail())
            .phoneNumber(person.getPhoneNumber())
            .address(person.getAddress())
            .city(person.getCity())
            .country(person.getCountry())
            .status(person.getStatus().getCode())
            .createdDate(person.getCreatedDate())
            .updatedDate(person.getUpdatedDate())
            .build();
    }

    private Person entityToDomain(PersonEntity entity) {
        return Person.from(
            entity.getId(),
            entity.getDocumentType(),
            entity.getDocumentNumber(),
            entity.getFirstName(),
            entity.getSecondName(),
            entity.getFirstLastName(),
            entity.getSecondLastName(),
            entity.getBirthDate(),
            entity.getEmail(),
            entity.getPhoneNumber(),
            entity.getAddress(),
            entity.getCity(),
            entity.getCountry(),
            PersonStatus.fromCode(entity.getStatus()),
            entity.getCreatedDate(),
            entity.getUpdatedDate()
        );
    }
}