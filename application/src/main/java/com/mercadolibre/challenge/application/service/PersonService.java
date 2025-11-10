package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.PersonListResponse;
import com.mercadolibre.challenge.application.dto.PersonRequest;
import com.mercadolibre.challenge.application.dto.PersonResponse;
import com.mercadolibre.challenge.application.port.in.CreatePersonUseCase;
import com.mercadolibre.challenge.application.port.in.DeletePersonUseCase;
import com.mercadolibre.challenge.application.port.in.GetPersonUseCase;
import com.mercadolibre.challenge.application.port.in.UpdatePersonUseCase;
import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;
import com.mercadolibre.challenge.domain.common.exception.person.PersonAlreadyExistsException;
import com.mercadolibre.challenge.domain.common.exception.person.PersonNotFoundException;
import com.mercadolibre.challenge.domain.person.Person;
import com.mercadolibre.challenge.domain.person.PersonStatus;
import com.mercadolibre.challenge.domain.person.repository.PersonCommandRepository;
import com.mercadolibre.challenge.domain.person.repository.PersonQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestión de personas
 * Implementa CQRS usando PersonCommandRepository y PersonQueryRepository
 *
 * Ventajas de este enfoque:
 * - ISP: Solo depende de las operaciones que realmente necesita
 * - Transacciones optimizadas: @Transactional solo en métodos de escritura
 * - Lectura de réplicas: Los queries pueden dirigirse a BD de solo lectura en el futuro
 */
@Service
public class PersonService implements CreatePersonUseCase, UpdatePersonUseCase,
        GetPersonUseCase, DeletePersonUseCase {

    private final PersonCommandRepository commandRepository;
    private final PersonQueryRepository queryRepository;

    public PersonService(PersonCommandRepository commandRepository,
                        PersonQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    // ==================== CREATE ====================

    @Override
    @Transactional
    public PersonResponse createPerson(PersonRequest request) {
        ValidateArgument.validateNotNull(request, "request");

        // Validar que no exista una persona con el mismo documento (Query)
        if (queryRepository.existsByDocument(request.getDocumentType(), request.getDocumentNumber())) {
            throw new PersonAlreadyExistsException(request.getDocumentType(), request.getDocumentNumber());
        }

        // Validar que no exista una persona con el mismo email (Query)
        if (queryRepository.existsByEmail(request.getEmail())) {
            throw new PersonAlreadyExistsException(request.getEmail());
        }

        // Crear la entidad de dominio
        Person person = Person.create(
            request.getDocumentType(),
            request.getDocumentNumber(),
            request.getFirstName(),
            request.getSecondName(),
            request.getFirstLastName(),
            request.getSecondLastName(),
            request.getBirthDate(),
            request.getEmail(),
            request.getPhoneNumber(),
            request.getAddress(),
            request.getCity(),
            request.getCountry()
        );

        // Guardar (Command)
        Person savedPerson = commandRepository.save(person);

        return mapToResponse(savedPerson);
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public PersonResponse updatePerson(Long id, PersonRequest request) {
        ValidateArgument.validateNotNull(id, "id");
        ValidateArgument.validateNotNull(request, "request");

        // Buscar la persona (Query)
        Person person = queryRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));

        // Validar que el email no esté siendo usado por otra persona (Query)
        queryRepository.findByEmail(request.getEmail())
            .ifPresent(existingPerson -> {
                if (!existingPerson.getId().equals(id)) {
                    throw new PersonAlreadyExistsException(request.getEmail());
                }
            });

        // Actualizar
        Person updatedPerson = person.update(
            request.getFirstName(),
            request.getSecondName(),
            request.getFirstLastName(),
            request.getSecondLastName(),
            request.getEmail(),
            request.getPhoneNumber(),
            request.getAddress(),
            request.getCity(),
            request.getCountry()
        );

        // Guardar actualización (Command)
        Person savedPerson = commandRepository.update(updatedPerson);

        return mapToResponse(savedPerson);
    }

    // ==================== GET (Solo Query Repository) ====================

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long id) {
        ValidateArgument.validateNotNull(id, "id");

        // Solo lectura - usar queryRepository
        Person person = queryRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));

        return mapToResponse(person);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPersonByDocument(String documentType, String documentNumber) {
        ValidateArgument.validateStringNotNullAndNotEmpty(documentType, "documentType");
        ValidateArgument.validateStringNotNullAndNotEmpty(documentNumber, "documentNumber");

        // Solo lectura - usar queryRepository
        Person person = queryRepository.findByDocument(documentType, documentNumber)
            .orElseThrow(() -> new PersonNotFoundException(documentType, documentNumber));

        return mapToResponse(person);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonListResponse getAllPersons(int offset, int limit) {
        validatePagination(offset, limit);

        // Solo lectura - usar queryRepository
        List<Person> persons = queryRepository.findAll(offset, limit);
        long total = queryRepository.count();

        return PersonListResponse.builder()
            .persons(persons.stream().map(this::mapToResponse).collect(Collectors.toList()))
            .total((int) total)
            .offset(offset)
            .limit(limit)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonListResponse searchPersonsByName(String name, int offset, int limit) {
        ValidateArgument.validateStringNotNullAndNotEmpty(name, "name");
        validatePagination(offset, limit);

        // Solo lectura - usar queryRepository
        List<Person> persons = queryRepository.findByName(name, offset, limit);
        long total = persons.size(); // Simplificado, en producción debería tener un count específico

        return PersonListResponse.builder()
            .persons(persons.stream().map(this::mapToResponse).collect(Collectors.toList()))
            .total((int) total)
            .offset(offset)
            .limit(limit)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonListResponse getPersonsByStatus(String status, int offset, int limit) {
        ValidateArgument.validateStringNotNullAndNotEmpty(status, "status");
        validatePagination(offset, limit);

        // Solo lectura - usar queryRepository
        List<Person> persons = queryRepository.findByStatus(status, offset, limit);
        long total = queryRepository.countByStatus(status);

        return PersonListResponse.builder()
            .persons(persons.stream().map(this::mapToResponse).collect(Collectors.toList()))
            .total((int) total)
            .offset(offset)
            .limit(limit)
            .build();
    }

    // ==================== DELETE (Query + Command) ====================

    @Override
    @Transactional
    public void deactivatePerson(Long id) {
        ValidateArgument.validateNotNull(id, "id");

        // Lectura (Query)
        Person person = queryRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));

        // Modificación + escritura (Command)
        Person deactivated = person.deactivate();
        commandRepository.update(deactivated);
    }

    @Override
    @Transactional
    public void activatePerson(Long id) {
        ValidateArgument.validateNotNull(id, "id");

        // Lectura (Query)
        Person person = queryRepository.findById(id)
            .orElseThrow(() -> new PersonNotFoundException(id));

        // Modificación + escritura (Command)
        Person activated = person.activate();
        commandRepository.update(activated);
    }

    @Override
    @Transactional
    public void deletePerson(Long id) {
        ValidateArgument.validateNotNull(id, "id");

        // Verificar existencia (Query)
        if (!queryRepository.findById(id).isPresent()) {
            throw new PersonNotFoundException(id);
        }

        // Eliminación (Command)
        commandRepository.deleteById(id);
    }

    // ==================== MAPPERS ====================

    private PersonResponse mapToResponse(Person person) {
        return PersonResponse.builder()
            .id(person.getId())
            .documentType(person.getDocumentType())
            .documentNumber(person.getDocumentNumber())
            .firstName(person.getFirstName())
            .secondName(person.getSecondName())
            .firstLastName(person.getFirstLastName())
            .secondLastName(person.getSecondLastName())
            .fullName(person.getFullName())
            .birthDate(person.getBirthDate())
            .age(person.getAge())
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

    // ==================== VALIDATIONS ====================

    private void validatePagination(int offset, int limit) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset no puede ser negativo");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit debe ser mayor a 0");
        }
        if (limit > 100) {
            throw new IllegalArgumentException("Limit no puede ser mayor a 100");
        }
    }
}