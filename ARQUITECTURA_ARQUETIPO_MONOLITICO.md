# ARQUETIPO DE ARQUITECTURA HEXAGONAL CON DDD - SPRING BOOT MONOLÍTICO

## PROPÓSITO DEL DOCUMENTO

Este documento define un arquetipo arquitectónico completo para proyectos Java con Spring Boot que implementan Arquitectura Hexagonal (Ports & Adapters) y principios de Domain-Driven Design (DDD) en un **único módulo** (sin modularidad de Gradle). El objetivo es que pueda ser usado como plantilla para crear nuevos proyectos con la misma estructura, patrones y buenas prácticas, **sin replicar la lógica de negocio específica**.

---

## 1. ESTRUCTURA DEL PROYECTO

### 1.1 Organización Monolítica con Separación por Paquetes

El proyecto se organiza como un **único módulo Spring Boot** con separación clara de responsabilidades mediante paquetes:

```
root-project/
├── build.gradle                    # Configuración única de Gradle
├── settings.gradle                 # Nombre del proyecto
├── gradlew                         # Wrapper de Gradle
├── gradlew.bat
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── empresa/
    │   │           └── proyecto/
    │   │               ├── Application.java         # Clase principal
    │   │               ├── domain/                  # Capa de Dominio
    │   │               │   ├── common/
    │   │               │   │   ├── exception/
    │   │               │   │   └── utils/
    │   │               │   └── [aggregate]/
    │   │               │       ├── [Entity].java
    │   │               │       ├── [ValueObject].java
    │   │               │       └── repository/
    │   │               │           └── [Entity]Repository.java
    │   │               ├── application/             # Capa de Aplicación
    │   │               │   ├── dto/
    │   │               │   ├── port/
    │   │               │   │   └── in/
    │   │               │   └── service/
    │   │               └── infrastructure/          # Capa de Infraestructura
    │   │                   ├── adapter/
    │   │                   │   ├── persistence/     # Driven Adapters
    │   │                   │   │   ├── [entity]/
    │   │                   │   │   │   ├── entity/
    │   │                   │   │   │   ├── repository/
    │   │                   │   │   │   └── adapter/
    │   │                   │   └── rest/            # Driver Adapters
    │   │                   │       ├── controller/
    │   │                   │       ├── exception/
    │   │                   │       └── response/
    │   │                   └── config/              # Configuraciones
    │   └── resources/
    │       ├── application.yml
    │       ├── application-dev.yml
    │       ├── application-prod.yml
    │       ├── schema.sql (opcional)
    │       └── data.sql (opcional)
    └── test/
        └── java/
            └── com/
                └── empresa/
                    └── proyecto/
                        ├── domain/
                        ├── application/
                        └── infrastructure/
```

### 1.2 Configuración del settings.gradle

```gradle
rootProject.name = 'nombre-proyecto'
```

### 1.3 Configuración del build.gradle

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.5'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.empresa.proyecto'
version = '1.0-SNAPSHOT'
sourceCompatibility = '17'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    runtimeOnly 'com.h2database:h2'
    // runtimeOnly 'org.postgresql:postgresql'  // Para PostgreSQL
    // runtimeOnly 'mysql:mysql-connector-java' // Para MySQL

    // Lombok
    compileOnly 'org.projectlombok:lombok:1.18.38'
    annotationProcessor 'org.projectlombok:lombok:1.18.38'

    // MapStruct (opcional para mapeo de objetos)
    implementation 'org.mapstruct:mapstruct:1.6.3'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testImplementation 'org.mockito:mockito-core'
    testImplementation 'org.mockito:mockito-junit-jupiter'
    testImplementation 'org.assertj:assertj-core'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}
```

---

## 2. CAPA DE DOMINIO (domain/)

### 2.1 Propósito

La capa de dominio contiene:
- **Modelos de dominio** (entidades, value objects, agregados)
- **Puertos de salida** (interfaces de repositorios)
- **Excepciones de dominio**
- **Lógica de negocio central**
- **Utilidades de dominio**

**IMPORTANTE**: Esta capa NO debe tener dependencias de Spring, JPA, o cualquier framework externo. Es puro Java.

### 2.2 Estructura de Paquetes

```
com.empresa.proyecto.domain/
├── common/
│   ├── exception/
│   │   ├── BaseException.java
│   │   ├── BusinessException.java
│   │   ├── ValidationException.java
│   │   ├── BaseNotFoundException.java
│   │   ├── ValidateArgument.java
│   │   └── codes/
│   │       └── ExceptionCode.java
│   └── utils/
│       ├── CodedEnum.java
│       └── EnumUtils.java
└── [aggregate]/
    ├── [Entity].java
    ├── [ValueObject].java
    └── repository/
        └── [Entity]Repository.java
```

### 2.3 Patrón de Excepción Base

```java
package com.empresa.proyecto.domain.common.exception;

/**
 * Excepción base para todos los errores del dominio
 * Incluye código de error y mensaje personalizado
 */
public class BaseException extends RuntimeException {

    private final String errorCode;
    private final String userMessage;

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public BaseException(String errorCode, String message, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public BaseException(String errorCode, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
```

### 2.4 Excepción de Validación

```java
package com.empresa.proyecto.domain.common.exception;

import com.empresa.proyecto.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción para errores de validación de datos
 */
public class ValidationException extends BaseException {

    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationException(String errorCode, String message, String userMessage) {
        super(errorCode, message, userMessage);
    }

    public static ValidationException nullField(String fieldName) {
        return new ValidationException(
            ExceptionCode.NULL_FIELD,
            "Campo nulo o vacio: " + fieldName,
            "El campo " + fieldName + " es obligatorio"
        );
    }

    public static ValidationException emptyField(String fieldName) {
        return new ValidationException(
            ExceptionCode.EMPTY_FIELD,
            "Campo vacío: " + fieldName,
            "El campo " + fieldName + " no puede estar vacío"
        );
    }

    public static ValidationException invalidValue(String fieldName, String value) {
        return new ValidationException(
            ExceptionCode.INVALID_VALUE,
            "Valor inválido para " + fieldName + ": " + value,
            "El valor proporcionado para " + fieldName + " no es válido"
        );
    }

    public static ValidationException invalidRange(String fieldName, String details) {
        return new ValidationException(
            ExceptionCode.INVALID_RANGE,
            "Valor fuera de rango para " + fieldName + ": " + details,
            "El valor de " + fieldName + " está fuera del rango permitido"
        );
    }

    public static ValidationException invalidLength(String fieldName, int current, int max) {
        return new ValidationException(
            ExceptionCode.INVALID_LENGTH,
            String.format("Longitud inválida para %s: %d (máximo %d)", fieldName, current, max),
            String.format("El campo %s excede la longitud máxima permitida", fieldName)
        );
    }
}
```

### 2.5 Excepción de Negocio

```java
package com.empresa.proyecto.domain.common.exception;

import com.empresa.proyecto.domain.common.exception.codes.ExceptionCode;

/**
 * Excepción para errores de lógica de negocio
 */
public class BusinessException extends BaseException {

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(String errorCode, String message, String userMessage) {
        super(errorCode, message, userMessage);
    }

    public static BusinessException resourceConflict(String resourceName, String details) {
        return new BusinessException(
            ExceptionCode.RESOURCE_CONFLICT,
            "Conflicto con recurso " + resourceName + ": " + details,
            "La operación no se puede realizar debido a un conflicto con los datos existentes"
        );
    }

    public static BusinessException operationNotAllowed(String operation, String reason) {
        return new BusinessException(
            ExceptionCode.OPERATION_NOT_ALLOWED,
            "Operación no permitida '" + operation + "': " + reason,
            "No se puede realizar la operación solicitada en el estado actual"
        );
    }

    public static BusinessException resourceNotFound(String resourceName, String identifier) {
        return new BusinessException(
            ExceptionCode.RESOURCE_NOT_FOUND,
            resourceName + " no encontrado: " + identifier,
            "El recurso solicitado no existe"
        );
    }
}
```

### 2.6 Excepción Base Not Found

```java
package com.empresa.proyecto.domain.common.exception;

/**
 * Excepción base para recursos no encontrados
 */
public abstract class BaseNotFoundException extends BaseException {

    public BaseNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BaseNotFoundException(String errorCode, String message, String userMessage) {
        super(errorCode, message, userMessage);
    }
}
```

### 2.7 Códigos de Excepción Centralizados

```java
package com.empresa.proyecto.domain.common.exception.codes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionCode {
    // Validación de datos (ERR-001 a ERR-007)
    public static final String NULL_FIELD = "ERR-001";
    public static final String EMPTY_FIELD = "ERR-002";
    public static final String INVALID_VALUE = "ERR-003";
    public static final String INVALID_FORMAT = "ERR-004";
    public static final String INVALID_LENGTH = "ERR-005";
    public static final String INVALID_RANGE = "ERR-006";
    public static final String VALIDATION_ERROR = "ERR-007";

    // Autenticación y autorización (ERR-101 a ERR-105)
    public static final String UNAUTHORIZED = "ERR-101";
    public static final String FORBIDDEN = "ERR-102";
    public static final String INVALID_CREDENTIALS = "ERR-103";
    public static final String TOKEN_EXPIRED = "ERR-104";
    public static final String TOKEN_INVALID = "ERR-105";

    // Recursos y entidades (ERR-201 a ERR-204)
    public static final String RESOURCE_NOT_FOUND = "ERR-201";
    public static final String RESOURCE_ALREADY_EXISTS = "ERR-202";
    public static final String RESOURCE_CONFLICT = "ERR-203";
    public static final String RESOURCE_LOCKED = "ERR-204";

    // Base de datos (ERR-401 a ERR-404)
    public static final String DATABASE_ERROR = "ERR-401";
    public static final String CONNECTION_ERROR = "ERR-402";
    public static final String TRANSACTION_FAILED = "ERR-403";
    public static final String CONSTRAINT_VIOLATION = "ERR-404";

    // Servicios externos (ERR-501 a ERR-504)
    public static final String EXTERNAL_SERVICE_ERROR = "ERR-501";
    public static final String SERVICE_UNAVAILABLE = "ERR-502";
    public static final String TIMEOUT_ERROR = "ERR-503";
    public static final String API_LIMIT_EXCEEDED = "ERR-504";

    // Operaciones (ERR-801 a ERR-804)
    public static final String OPERATION_NOT_ALLOWED = "ERR-801";
    public static final String OPERATION_FAILED = "ERR-802";
    public static final String CONCURRENT_MODIFICATION = "ERR-803";
    public static final String RATE_LIMIT_EXCEEDED = "ERR-804";

    // Sistema (ERR-901 a ERR-905)
    public static final String INTERNAL_SERVER_ERROR = "ERR-901";
    public static final String CONFIGURATION_ERROR = "ERR-902";
    public static final String FILE_NOT_FOUND = "ERR-903";
    public static final String FILE_READ_ERROR = "ERR-904";
    public static final String SERIALIZATION_ERROR = "ERR-905";
}
```

### 2.8 Utilidad de Validación

```java
package com.empresa.proyecto.domain.common.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateArgument {

    public void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw ValidationException.nullField(fieldName);
        }
    }

    public void validateStringNotNullAndNotEmpty(String valor, String fieldName) {
        if (valor == null || valor.isEmpty()) {
            throw ValidationException.nullField(fieldName);
        }
    }

    public void validateLength(int number, int min, int max, String fieldName) {
        if (number < min || number > max) {
            throw ValidationException.invalidLength(fieldName, number, max);
        }
    }
}
```

### 2.9 Patrón de Entidad de Dominio

Las entidades de dominio deben seguir estos principios:

```java
package com.empresa.proyecto.domain.[aggregate];

import com.empresa.proyecto.domain.common.exception.ValidateArgument;
import java.util.*;

public class [Entity] {

    // Campos privados finales (inmutabilidad)
    private final String id;
    private final String name;
    // ... otros campos

    // Constructor privado
    private [Entity](String id, String name, ...) {
        this.id = id;
        this.name = name;
        // ...
    }

    // Factory method público con validación
    public static [Entity] from(String id, String name, ...) {
        validateParameters(id, name);

        return new [Entity](
            id.trim(),
            name.trim(),
            // ...
        );
    }

    // Validación centralizada
    private static void validateParameters(String id, String name) {
        ValidateArgument.validateStringNotNullAndNotEmpty(id, "id");
        ValidateArgument.validateStringNotNullAndNotEmpty(name, "name");
        ValidateArgument.validateLength(id.length(), 1, 50, "id");
    }

    // Getters públicos (sin setters para inmutabilidad)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Métodos de comportamiento de dominio
    public boolean isActive() {
        // Lógica de dominio
    }

    // equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        [Entity] entity = ([Entity]) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[Entity]{id='%s', name='%s'}", id, name);
    }
}
```

### 2.10 Patrón de Repository (Puerto de Salida)

```java
package com.empresa.proyecto.domain.[aggregate].repository;

import com.empresa.proyecto.domain.[aggregate].[Entity];
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida - Interfaz de repositorio del dominio
 * NO contiene anotaciones de Spring, JPA u otro framework
 */
public interface [Entity]Repository {

    Optional<[Entity]> findById(String id);

    List<[Entity]> findAll();

    [Entity] save([Entity] entity);

    void deleteById(String id);

    // Métodos específicos del dominio
}
```

---

## 3. CAPA DE APLICACIÓN (application/)

### 3.1 Propósito

La capa de aplicación contiene:
- **Casos de uso** (servicios de aplicación)
- **Puertos de entrada** (interfaces de casos de uso)
- **DTOs** (objetos de transferencia de datos)
- **Mappers** (transformaciones entre dominio y DTOs)

### 3.2 Estructura de Paquetes

```
com.empresa.proyecto.application/
├── dto/
│   ├── [UseCase]Request.java
│   └── [UseCase]Response.java
├── port/
│   └── in/
│       └── [UseCase].java
└── service/
    └── [UseCase]Service.java
```

### 3.3 Patrón de Puerto de Entrada (Use Case Interface)

```java
package com.empresa.proyecto.application.port.in;

import com.empresa.proyecto.application.dto.[UseCase]Response;

/**
 * Puerto de entrada - Define el caso de uso
 */
public interface [UseCase] {

    [UseCase]Response execute(String param);

}
```

### 3.4 Patrón de Servicio de Aplicación

```java
package com.empresa.proyecto.application.service;

import com.empresa.proyecto.application.dto.[UseCase]Response;
import com.empresa.proyecto.application.port.in.[UseCase];
import com.empresa.proyecto.domain.common.exception.ValidateArgument;
import com.empresa.proyecto.domain.[aggregate].[Entity];
import com.empresa.proyecto.domain.[aggregate].repository.[Entity]Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class [UseCase]Service implements [UseCase] {

    private final [Entity]Repository repository;

    public [UseCase]Service([Entity]Repository repository) {
        this.repository = repository;
    }

    @Override
    public [UseCase]Response execute(String param) {
        ValidateArgument.validateStringNotNullAndNotEmpty(param, "param");

        [Entity] entity = repository.findById(param)
                .orElseThrow(() -> new [Entity]NotFoundException(param));

        return mapToResponse(entity);
    }

    private [UseCase]Response mapToResponse([Entity] entity) {
        [UseCase]Response response = new [UseCase]Response();
        response.setId(entity.getId());
        response.setName(entity.getName());
        // ... mapeo de campos
        return response;
    }
}
```

### 3.5 Patrón de DTOs

```java
package com.empresa.proyecto.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class [UseCase]Response {

    private String id;
    private String name;
    // ... campos necesarios

    // DTOs anidados si es necesario
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NestedDto {
        private String field;
    }
}
```

---

## 4. CAPA DE INFRAESTRUCTURA (infrastructure/)

### 4.1 Propósito

La capa de infraestructura contiene:
- **Adaptadores Driven** (secundarios): Implementaciones de puertos de salida
- **Adaptadores Driver** (primarios): Puntos de entrada a la aplicación
- **Configuraciones**: Beans, seguridad, etc.

### 4.2 Estructura de Paquetes

```
com.empresa.proyecto.infrastructure/
├── adapter/
│   ├── persistence/                    # Adaptadores Driven (BD)
│   │   └── [entity]/
│   │       ├── entity/
│   │       │   └── [Entity]Entity.java
│   │       ├── repository/
│   │       │   └── Jpa[Entity]Repository.java
│   │       └── adapter/
│   │           └── [Entity]RepositoryAdapter.java
│   └── rest/                          # Adaptadores Driver (REST)
│       ├── controller/
│       │   └── [Entity]Controller.java
│       ├── exception/
│       │   └── GlobalExceptionHandler.java
│       └── response/
│           ├── ApiResponse.java
│           └── ValidationErrorResponse.java
└── config/                            # Configuraciones
    ├── DatabaseConfig.java
    └── WebConfig.java
```

---

## 5. ADAPTADORES DRIVEN (infrastructure/adapter/persistence/)

### 5.1 Patrón de Adaptador de Repositorio

```java
package com.empresa.proyecto.infrastructure.adapter.persistence.[entity].adapter;

import com.empresa.proyecto.domain.[aggregate].[Entity];
import com.empresa.proyecto.domain.[aggregate].repository.[Entity]Repository;
import com.empresa.proyecto.infrastructure.adapter.persistence.[entity].entity.[Entity]Entity;
import com.empresa.proyecto.infrastructure.adapter.persistence.[entity].repository.Jpa[Entity]Repository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class [Entity]RepositoryAdapter implements [Entity]Repository {

    private final Jpa[Entity]Repository jpaRepository;

    public [Entity]RepositoryAdapter(Jpa[Entity]Repository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<[Entity]> findById(String id) {
        return jpaRepository.findById(id)
                .map(this::entityToDomain);
    }

    @Override
    public List<[Entity]> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public [Entity] save([Entity] entity) {
        [Entity]Entity entityToSave = domainToEntity(entity);
        [Entity]Entity savedEntity = jpaRepository.save(entityToSave);
        return entityToDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    // Métodos de conversión
    private [Entity] entityToDomain([Entity]Entity entity) {
        return [Entity].from(
                entity.getId(),
                entity.getName(),
                // ... todos los campos
        );
    }

    private [Entity]Entity domainToEntity([Entity] domain) {
        [Entity]Entity entity = new [Entity]Entity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        // ... todos los campos
        return entity;
    }
}
```

### 5.2 Patrón de Entidad JPA

```java
package com.empresa.proyecto.infrastructure.adapter.persistence.[entity].entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "[table_name]")
@Getter
@Setter
@NoArgsConstructor
public class [Entity]Entity {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_id")
    private RelatedEntity related;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChildEntity> children;
}
```

### 5.3 Patrón de Repositorio JPA

```java
package com.empresa.proyecto.infrastructure.adapter.persistence.[entity].repository;

import com.empresa.proyecto.infrastructure.adapter.persistence.[entity].entity.[Entity]Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface Jpa[Entity]Repository extends JpaRepository<[Entity]Entity, String> {

    @Query("SELECT e FROM [Entity]Entity e WHERE e.name = :name")
    Optional<[Entity]Entity> findByName(@Param("name") String name);

    @Query("SELECT e FROM [Entity]Entity e WHERE e.status = 'ACTIVE'")
    List<[Entity]Entity> findAllActive();
}
```

---

## 6. ADAPTADORES DRIVER (infrastructure/adapter/rest/)

### 6.1 Patrón de Controlador REST

```java
package com.empresa.proyecto.infrastructure.adapter.rest.controller;

import com.empresa.proyecto.application.dto.[UseCase]Response;
import com.empresa.proyecto.application.port.in.[UseCase];
import com.empresa.proyecto.infrastructure.adapter.rest.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/[entities]")
@CrossOrigin(origins = "*")
public class [Entity]Controller {

    private final [UseCase] useCase;

    public [Entity]Controller([UseCase] useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<[UseCase]Response>> getById(@PathVariable("id") String id) {
        [UseCase]Response response = useCase.execute(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Operación exitosa")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<[UseCase]Response>> create(@RequestBody @Valid [UseCase]Request request) {
        [UseCase]Response response = useCase.execute(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Creado exitosamente")
        );
    }
}
```

### 6.2 Patrón de Respuesta Estándar (ApiResponse)

```java
package com.empresa.proyecto.infrastructure.adapter.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Respuesta estándar de la API
 * @param <T> Tipo de datos en la respuesta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private String description;
    private String errorCode;
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = "Operación exitosa";
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.errorCode = errorCode;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String errorCode, String message, String description) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.errorCode = errorCode;
        response.message = message;
        response.description = description;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        return response;
    }
}
```

### 6.3 GlobalExceptionHandler (CRÍTICO)

```java
package com.empresa.proyecto.infrastructure.adapter.rest.exception;

import com.empresa.proyecto.domain.common.exception.BaseException;
import com.empresa.proyecto.domain.common.exception.ValidationException;
import com.empresa.proyecto.domain.common.exception.BusinessException;
import com.empresa.proyecto.domain.common.exception.codes.ExceptionCode;
import com.empresa.proyecto.domain.common.exception.BaseNotFoundException;
import com.empresa.proyecto.infrastructure.adapter.rest.response.ApiResponse;
import com.empresa.proyecto.infrastructure.adapter.rest.response.ValidationErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST
 *
 * PRINCIPIOS:
 * 1. Centralización: Todas las excepciones se manejan aquí
 * 2. Consistencia: Todas las respuestas siguen el mismo formato
 * 3. Trazabilidad: Códigos únicos para cada tipo de error
 * 4. Separación: Mensajes técnicos vs mensajes de usuario
 */
@RestControllerAdvice
@Component
public class GlobalExceptionHandler {

    private <T> ResponseEntity<T> json(HttpStatus status, T body) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            fieldErrors.computeIfAbsent(field, k -> new ArrayList<>())
                      .add(error.getDefaultMessage());
        });

        ValidationErrorResponse response = ValidationErrorResponse.create(
                ExceptionCode.VALIDATION_ERROR,
                "Error de validación en los datos",
                fieldErrors
        );
        return json(HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            ValidationException ex,
            WebRequest r) {
        return json(HttpStatus.BAD_REQUEST,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex,
            WebRequest r) {
        return json(HttpStatus.CONFLICT,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BaseNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(
            BaseNotFoundException ex,
            WebRequest r) {
        return json(HttpStatus.NOT_FOUND,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(
            BaseException ex,
            WebRequest r) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR,
                   ApiResponse.error(ex.getErrorCode(), ex.getUserMessage(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest r) {
        return json(HttpStatus.BAD_REQUEST,
                   ApiResponse.error("INVALID_ARGUMENT", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(
            IllegalStateException ex,
            WebRequest r) {
        return json(HttpStatus.BAD_REQUEST,
                   ApiResponse.error("INVALID_STATE", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            WebRequest r) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR,
                   ApiResponse.error("INTERNAL_SERVER_ERROR",
                                    "Ha ocurrido un error interno del servidor"));
    }
}
```

### 6.4 ValidationErrorResponse

```java
package com.empresa.proyecto.infrastructure.adapter.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ValidationErrorResponse {

    private Boolean success;
    private String errorCode;
    private String message;
    private Map<String, List<String>> fieldErrors;
    private LocalDateTime timestamp;

    public ValidationErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.success = false;
    }

    public static ValidationErrorResponse create(String errorCode,
                                                  String message,
                                                  Map<String, List<String>> fieldErrors) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.errorCode = errorCode;
        response.message = message;
        response.fieldErrors = fieldErrors;
        return response;
    }
}
```

---

## 7. CLASE PRINCIPAL Y CONFIGURACIÓN

### 7.1 Clase Principal de Aplicación

```java
package com.empresa.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 7.2 Configuración application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1/

spring:
  application:
    name: nombre-proyecto

  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:

  h2:
    console:
      enabled: true
      path: /h2-console

  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect

  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
      continue-on-error: false

logging:
  level:
    com.empresa.proyecto: DEBUG
    org.springframework.web: DEBUG
    org.springframework.boot.sql.init: DEBUG
    org.springframework.jdbc.datasource.init: DEBUG
```

---

## 8. PATRONES Y BUENAS PRÁCTICAS

### 8.1 Principios de Código Limpio

#### Inmutabilidad
- Usar campos `final` siempre que sea posible
- Evitar setters en entidades de dominio
- Usar factory methods en lugar de constructores públicos

#### Validación Centralizada
- Todas las validaciones en el dominio usando `ValidateArgument`
- Validaciones en factory methods antes de crear objetos
- Excepciones específicas para cada tipo de error

#### Separación de Responsabilidades
- **domain/**: Lógica de negocio pura (sin Spring, sin JPA)
- **application/**: Orquestación de casos de uso
- **infrastructure/**: Detalles técnicos (Spring, JPA, REST)

#### Inyección de Dependencias
- Constructor injection (no field injection)
- Interfaces para todos los puertos
- No usar @Autowired explícito

### 8.2 Dependencias entre Capas

```
┌─────────────────────────────────────┐
│       INFRASTRUCTURE                │
│  ┌──────────┐    ┌──────────┐      │
│  │   REST   │    │Persistence│      │
│  │(Driver)  │    │ (Driven)  │      │
│  └────┬─────┘    └─────┬────┘      │
└───────┼────────────────┼────────────┘
        │                │
        │          ┌─────▼──────┐
        │          │APPLICATION │
        │          └─────┬──────┘
        │                │
        │          ┌─────▼──────┐
        └──────────►   DOMAIN   │ ← Núcleo (sin dependencias)
                   └────────────┘
```

**Reglas de Dependencia:**
1. **domain/**: NO depende de nadie (Java puro)
2. **application/**: Solo depende de domain/
3. **infrastructure/**: Depende de domain/ y application/

### 8.3 Naming Conventions

#### Paquetes
- `domain.[aggregate]` - Agregados del dominio
- `domain.[aggregate].repository` - Puertos de salida
- `domain.common.exception` - Excepciones de dominio
- `application.port.in` - Puertos de entrada
- `application.service` - Implementación de casos de uso
- `application.dto` - Objetos de transferencia
- `infrastructure.adapter.persistence.[entity].adapter` - Implementación de repositorios
- `infrastructure.adapter.persistence.[entity].entity` - Entidades JPA
- `infrastructure.adapter.persistence.[entity].repository` - Repositorios JPA
- `infrastructure.adapter.rest.controller` - Controladores REST
- `infrastructure.adapter.rest.exception` - Manejo de excepciones REST
- `infrastructure.adapter.rest.response` - Respuestas estándar

#### Clases
- Entidades de Dominio: `[Entity]` (ej: `Item`, `User`, `Order`)
- Repositorios de Dominio: `[Entity]Repository` (interfaz)
- Casos de Uso: `[Verb][Entity]UseCase` (ej: `GetItemDetailUseCase`)
- Servicios: `[Entity][Action]Service` (ej: `ItemDetailService`)
- DTOs: `[UseCase]Request`, `[UseCase]Response`
- Entidades JPA: `[Entity]Entity` (ej: `ItemEntity`)
- Adaptadores: `[Entity]RepositoryAdapter`
- Controladores: `[Entity]Controller`
- Excepciones: `[Entity]NotFoundException`, `[Concept]Exception`

### 8.4 Testing Strategy

#### Tests Unitarios de Dominio
- Testear validaciones
- Testear comportamientos de negocio
- Sin dependencias de Spring
- Ubicación: `test/java/.../domain/`

#### Tests Unitarios de Aplicación
- Testear casos de uso con mocks
- Verificar orquestación
- Usar Mockito
- Ubicación: `test/java/.../application/`

#### Tests de Integración de Infraestructura
- Testear adaptadores con base de datos real (H2)
- Testear controladores con MockMvc
- Usar @SpringBootTest
- Ubicación: `test/java/.../infrastructure/`

#### Tests End-to-End
- Testear flujos completos
- Usar TestRestTemplate

---

## 9. SISTEMA DE EXCEPCIONES - ARQUITECTURA COMPLETA

### 9.1 Jerarquía de Excepciones

```
RuntimeException
    └── BaseException (domain)
            ├── ValidationException (domain)
            ├── BusinessException (domain)
            └── BaseNotFoundException (domain)
                    ├── [Entity]NotFoundException (domain)
                    └── ...
```

### 9.2 Flujo de Manejo de Excepciones

```
1. Validación en Domain
   ↓
2. Lanzamiento de Excepción Tipada
   ↓
3. Propagación a través de Capas (sin try-catch)
   ↓
4. Captura en GlobalExceptionHandler (infrastructure/adapter/rest)
   ↓
5. Conversión a ApiResponse con HTTP Status
   ↓
6. Respuesta JSON Consistente al Cliente
```

### 9.3 Formato de Respuesta de Error

```json
{
  "success": false,
  "errorCode": "ERR-601",
  "message": "El ítem solicitado no existe",
  "description": "Item no encontrado: MLA123456789",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 9.4 Mapeo HTTP Status

| Excepción | HTTP Status | Uso |
|-----------|-------------|-----|
| ValidationException | 400 BAD_REQUEST | Datos inválidos |
| BusinessException | 409 CONFLICT | Reglas de negocio violadas |
| BaseNotFoundException | 404 NOT_FOUND | Recurso no encontrado |
| BaseException | 500 INTERNAL_SERVER_ERROR | Error genérico |
| IllegalArgumentException | 400 BAD_REQUEST | Argumento inválido |
| Exception | 500 INTERNAL_SERVER_ERROR | Error no controlado |

---

## 10. CONFIGURACIÓN DE BASE DE DATOS

### 10.1 Schema.sql (Definición de Estructura)

Ubicación: `src/main/resources/schema.sql`

```sql
-- Crear tablas con relaciones
CREATE TABLE IF NOT EXISTS [table_name] (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX idx_[table]_status ON [table_name](status);
CREATE INDEX idx_[table]_created ON [table_name](created_date);
```

### 10.2 Data.sql (Datos Iniciales)

Ubicación: `src/main/resources/data.sql`

```sql
-- Insertar datos de prueba
INSERT INTO [table_name] (id, name, status, created_date) VALUES
('ID001', 'Ejemplo 1', 'ACTIVE', CURRENT_TIMESTAMP),
('ID002', 'Ejemplo 2', 'ACTIVE', CURRENT_TIMESTAMP);
```

---

## 11. CHECKLIST DE IMPLEMENTACIÓN

### Creación de Nuevo Proyecto

- [ ] Crear proyecto Spring Boot con Gradle
- [ ] Configurar build.gradle con todas las dependencias
- [ ] Crear estructura de paquetes (domain, application, infrastructure)
- [ ] Crear paquete domain.common.exception con todas las excepciones base
- [ ] Implementar ExceptionCode completo
- [ ] Crear ValidateArgument utility
- [ ] Crear paquete application con port/in, service, dto
- [ ] Crear paquete infrastructure/adapter/persistence
- [ ] Crear paquete infrastructure/adapter/rest
- [ ] Implementar GlobalExceptionHandler
- [ ] Implementar ApiResponse y ValidationErrorResponse
- [ ] Configurar application.yml
- [ ] Crear Application.java (clase principal)

### Añadir Nuevo Agregado

- [ ] Crear paquete domain.[aggregate]
- [ ] Crear entidades de dominio con validaciones
- [ ] Crear value objects necesarios
- [ ] Crear excepción [Entity]NotFoundException en domain
- [ ] Crear interfaz [Entity]Repository en domain.[aggregate].repository
- [ ] Crear DTOs en application/dto
- [ ] Crear casos de uso en application/port/in
- [ ] Implementar servicios en application/service
- [ ] Crear paquete infrastructure/adapter/persistence/[entity]
- [ ] Crear entidades JPA en persistence/[entity]/entity
- [ ] Implementar JPA repository en persistence/[entity]/repository
- [ ] Implementar adapter en persistence/[entity]/adapter
- [ ] Crear controlador en infrastructure/adapter/rest/controller
- [ ] Escribir tests unitarios
- [ ] Escribir tests de integración

### Añadir Nuevo Endpoint

- [ ] Definir caso de uso (interfaz en application/port/in)
- [ ] Crear DTOs (Request/Response en application/dto)
- [ ] Implementar servicio (application/service)
- [ ] Crear método en controlador (infrastructure/adapter/rest/controller)
- [ ] Manejar excepciones específicas
- [ ] Escribir tests

---

## 12. EJEMPLO COMPLETO DE FLUJO

### Flujo de Consulta (Query)

```
1. HTTP Request
   GET /api/v1/items/MLA123

2. Controller (infrastructure/adapter/rest/controller)
   ItemController.getItemDetail("MLA123")

3. Use Case (application/port/in)
   GetItemDetailUseCase.getItemDetail("MLA123")

4. Service (application/service)
   ItemDetailService.getItemDetail("MLA123")
   - Valida parámetros
   - Llama al repositorio

5. Repository Port (domain/[aggregate]/repository)
   ItemRepository.findById("MLA123")

6. Repository Adapter (infrastructure/adapter/persistence)
   ItemRepositoryAdapter.findById("MLA123")
   - Consulta JPA
   - Convierte Entity → Domain

7. Retorno
   Domain → Service → UseCase → Controller → HTTP Response
```

### Flujo de Comando (Command)

```
1. HTTP Request
   POST /api/v1/items
   Body: { "title": "..." }

2. Controller (infrastructure/adapter/rest/controller)
   ItemController.createItem(request)

3. Use Case (application/port/in)
   CreateItemUseCase.execute(request)

4. Service (application/service)
   CreateItemService.execute(request)
   - Valida datos
   - Crea entidad de dominio (factory)
   - Guarda en repositorio

5. Domain Factory (domain/[aggregate])
   Item.from(...)
   - Valida reglas de negocio
   - Crea objeto inmutable

6. Repository Adapter (infrastructure/adapter/persistence)
   ItemRepositoryAdapter.save(item)
   - Convierte Domain → Entity
   - Guarda en JPA
   - Convierte Entity → Domain

7. Retorno
   Domain → Service → UseCase → Controller → HTTP Response
```

---

## 13. VENTAJAS DEL ARQUETIPO MONOLÍTICO

### Simplicidad
- Un solo módulo, más fácil de navegar
- No hay complejidad de multi-módulo Gradle
- Build más rápido
- Configuración más simple

### Mantenibilidad
- Separación clara de responsabilidades por paquetes
- Cambios localizados (cambiar BD no afecta dominio)
- Código autodocumentado
- Fácil refactoring

### Testabilidad
- Dominio testeable sin Spring
- Mocks fáciles por interfaces
- Tests de integración aislados
- Todo en un solo proyecto

### Escalabilidad
- Fácil añadir nuevos adaptadores
- Múltiples implementaciones del mismo puerto
- Si crece, fácil migrar a multi-módulo
- Preparado para microservicios

### Consistencia
- Manejo de errores unificado
- Respuestas API consistentes
- Códigos de error estandarizados
- Patrones uniformes

### Calidad
- Validaciones centralizadas
- Inmutabilidad por defecto
- Type-safety en compilación
- Separación de concerns

---

## 14. DIAGRAMA DE ARQUITECTURA VISUAL

```
┌─────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                         │
│  (infrastructure/adapter/)                                      │
├─────────────────────────┬───────────────────────────────────────┤
│   REST (Driver)         │         Persistence (Driven)          │
│                         │                                       │
│ ┌─────────────────────┐ │ ┌───────────────────────────────────┐ │
│ │  Controllers        │ │ │  Repository Adapters              │ │
│ │  - ItemController   │ │ │  - ItemRepositoryAdapter          │ │
│ │                     │ │ │                                   │ │
│ │  Exception Handler  │ │ │  JPA Repositories                 │ │
│ │  - Global Handler   │ │ │  - JpaItemRepository              │ │
│ │                     │ │ │                                   │ │
│ │  Response DTOs      │ │ │  JPA Entities                     │ │
│ │  - ApiResponse      │ │ │  - ItemEntity                     │ │
│ └─────────────────────┘ │ └───────────────────────────────────┘ │
└─────────┬───────────────┴──────────────┬──────────────────────────┘
          │                              │
┌─────────▼──────────────────────────────▼──────────────────────────┐
│                    APPLICATION LAYER                              │
│  (application/)                                                   │
│                                                                   │
│  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────┐ │
│  │  Use Cases       │  │  Services        │  │  DTOs          │ │
│  │  (Ports In)      │  │                  │  │                │ │
│  │                  │  │  - ItemDetail    │  │  - Request     │ │
│  │  - GetItemDetail │◄─┤    Service       │  │  - Response    │ │
│  │  - SearchItems   │  │  - SearchService │  │                │ │
│  └──────────────────┘  └──────────────────┘  └────────────────┘ │
└──────────────────────────────┬────────────────────────────────────┘
                               │
┌──────────────────────────────▼────────────────────────────────────┐
│                      DOMAIN LAYER                                 │
│  (domain/)                                                        │
│                                                                   │
│  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────┐ │
│  │  Entities        │  │  Repositories    │  │  Exceptions    │ │
│  │                  │  │  (Ports Out)     │  │                │ │
│  │  - Item          │  │                  │  │  - BaseEx...   │ │
│  │  - Seller        │  │  - ItemRepo      │  │  - Validation  │ │
│  │  - Category      │  │    (interface)   │  │  - Business    │ │
│  │                  │  │                  │  │  - NotFound    │ │
│  │  Value Objects   │  │                  │  │                │ │
│  │  - Price         │  │                  │  │  ExceptionCode │ │
│  └──────────────────┘  └──────────────────┘  └────────────────┘ │
│                                                                   │
│  Pure Java - No Spring, No JPA, No External Dependencies         │
└───────────────────────────────────────────────────────────────────┘
```

---

## 15. RESUMEN EJECUTIVO

Este arquetipo monolítico implementa:

1. **Arquitectura Hexagonal** con separación por paquetes
2. **Domain-Driven Design** con dominio puro y aislado
3. **Sistema de excepciones robusto** con códigos y mensajes duales
4. **Validaciones centralizadas** en el dominio
5. **Respuestas API consistentes** con ApiResponse
6. **Manejo global de errores** con GlobalExceptionHandler
7. **Inmutabilidad** en entidades de dominio
8. **Factory methods** para construcción segura
9. **Inyección por constructor** en toda la aplicación
10. **Separación de DTOs** para cada caso de uso

**Tecnologías Base:**
- Java 17
- Spring Boot 3.5.5
- Gradle 8.x
- JPA/Hibernate
- H2/PostgreSQL/MySQL
- JUnit 5 + Mockito

**Diferencias con el Arquetipo Modular:**
- ✅ Un solo módulo Gradle (más simple)
- ✅ Build más rápido
- ✅ Navegación más fácil
- ✅ Misma arquitectura hexagonal
- ✅ Mismas buenas prácticas
- ✅ Separación por paquetes en lugar de módulos

**Este arquetipo está listo para ser usado como plantilla en futuros proyectos, manteniendo la estructura pero reemplazando la lógica de negocio específica. Ideal para proyectos de tamaño pequeño a mediano que no requieren la complejidad de multi-módulo.**