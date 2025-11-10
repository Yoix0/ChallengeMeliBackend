package com.mercadolibre.challenge.infrastructure.rest.controller;

import com.mercadolibre.challenge.application.dto.PersonListResponse;
import com.mercadolibre.challenge.application.dto.PersonRequest;
import com.mercadolibre.challenge.application.dto.PersonResponse;
import com.mercadolibre.challenge.application.port.in.CreatePersonUseCase;
import com.mercadolibre.challenge.application.port.in.DeletePersonUseCase;
import com.mercadolibre.challenge.application.port.in.GetPersonUseCase;
import com.mercadolibre.challenge.application.port.in.UpdatePersonUseCase;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    private final CreatePersonUseCase createPersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final GetPersonUseCase getPersonUseCase;
    private final DeletePersonUseCase deletePersonUseCase;

    public PersonController(CreatePersonUseCase createPersonUseCase,
                           UpdatePersonUseCase updatePersonUseCase,
                           GetPersonUseCase getPersonUseCase,
                           DeletePersonUseCase deletePersonUseCase) {
        this.createPersonUseCase = createPersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.getPersonUseCase = getPersonUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
    }

    /**
     * Crear una nueva persona
     * POST /api/v1/persons
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PersonResponse>> createPerson(
            @RequestBody PersonRequest request
    ) {
        PersonResponse response = createPersonUseCase.createPerson(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Persona creada exitosamente"));
    }

    /**
     * Actualizar una persona existente
     * PUT /api/v1/persons/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonResponse>> updatePerson(
            @PathVariable("id") Long id,
            @RequestBody PersonRequest request
    ) {
        PersonResponse response = updatePersonUseCase.updatePerson(id, request);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Persona actualizada exitosamente")
        );
    }

    /**
     * Obtener una persona por ID
     * GET /api/v1/persons/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonResponse>> getPersonById(
            @PathVariable("id") Long id
    ) {
        PersonResponse response = getPersonUseCase.getPersonById(id);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Persona recuperada exitosamente")
        );
    }

    /**
     * Obtener una persona por documento
     * GET /api/v1/persons/document/{type}/{number}
     */
    @GetMapping("/document/{type}/{number}")
    public ResponseEntity<ApiResponse<PersonResponse>> getPersonByDocument(
            @PathVariable("type") String documentType,
            @PathVariable("number") String documentNumber
    ) {
        PersonResponse response = getPersonUseCase.getPersonByDocument(documentType, documentNumber);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Persona recuperada exitosamente")
        );
    }

    /**
     * Listar todas las personas con paginación
     * GET /api/v1/persons?offset=0&limit=20
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PersonListResponse>> getAllPersons(
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        PersonListResponse response = getPersonUseCase.getAllPersons(offset, limit);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Personas recuperadas exitosamente")
        );
    }

    /**
     * Buscar personas por nombre
     * GET /api/v1/persons/search?name=Juan&offset=0&limit=20
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PersonListResponse>> searchPersonsByName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        PersonListResponse response = getPersonUseCase.searchPersonsByName(name, offset, limit);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Búsqueda completada exitosamente")
        );
    }

    /**
     * Listar personas por estado
     * GET /api/v1/persons/status/ACTIVE?offset=0&limit=20
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PersonListResponse>> getPersonsByStatus(
            @PathVariable("status") String status,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        PersonListResponse response = getPersonUseCase.getPersonsByStatus(status, offset, limit);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Personas recuperadas exitosamente")
        );
    }

    /**
     * Inactivar una persona (soft delete)
     * PATCH /api/v1/persons/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivatePerson(
            @PathVariable("id") Long id
    ) {
        deletePersonUseCase.deactivatePerson(id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Persona inactivada exitosamente")
        );
    }

    /**
     * Activar una persona
     * PATCH /api/v1/persons/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activatePerson(
            @PathVariable("id") Long id
    ) {
        deletePersonUseCase.activatePerson(id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Persona activada exitosamente")
        );
    }

    /**
     * Eliminar permanentemente una persona (hard delete)
     * DELETE /api/v1/persons/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePerson(
            @PathVariable("id") Long id
    ) {
        deletePersonUseCase.deletePerson(id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Persona eliminada permanentemente")
        );
    }
}