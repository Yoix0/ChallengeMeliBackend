# ARQUETIPO DE ARQUITECTURA HEXAGONAL CON DDD - SPRING BOOT

## PROPÓSITO DEL DOCUMENTO

Este documento define un arquetipo arquitectónico completo para proyectos Java con Spring Boot que implementan Arquitectura Hexagonal (Ports & Adapters) y principios de Domain-Driven Design (DDD). El objetivo es que pueda ser usado como plantilla para crear nuevos proyectos con la misma estructura, patrones y buenas prácticas, **sin replicar la lógica de negocio específica**.

---

## 1. ESTRUCTURA DEL PROYECTO

### 1.1 Organización Multi-Módulo con Gradle

El proyecto debe organizarse como un **multi-módulo Gradle** con separación clara de responsabilidades:

```
root-project/
├── build.gradle                    # Configuración principal de Gradle
├── settings.gradle                 # Definición de módulos
├── gradlew                         # Wrapper de Gradle
├── gradlew.bat
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── domain/                         # Módulo del Dominio
├── application/                    # Módulo de Aplicación
├── infrastructure/                 # Módulo de Infraestructura
│   ├── driven-adapters/           # Adaptadores Driven (Secundarios)
│   │   └── [adapter-name]-driven/
│   └── driver-adapters/           # Adaptadores Driver (Primarios)
│       └── [adapter-name]-driver/
└── boot/                          # Módulo de Arranque
```

### 1.2 Configuración del settings.gradle

```gradle
rootProject.name = 'nombre-proyecto'
include ':domain'
include ':application'
include '[adapter-name]-driven'
include '[adapter-name]-driver'
include ':boot'

project(':domain').projectDir = file('./domain')
project(':application').projectDir = file('./application')
project(':[adapter-name]-driven').projectDir = file('./infrastructure/driven-adapters/[adapter-name]-driven')
project(':[adapter-name]-driver').projectDir = file('./infrastructure/driver-adapters/[adapter-name]-driver')
project(':boot').projectDir = file('./boot')
```

### 1.3 Configuración del build.gradle principal

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.5' apply false
    id 'io.spring.dependency-management' version '1.1.7' apply false
}

allprojects {
    group = 'com.empresa.proyecto'
    version = '1.0-SNAPSHOT'

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply plugin: 'java'
    apply plugin: 'io.spring.dependency-management'

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    ext {
        lombokVersion = '1.18.38'
    }

    dependencyManagement {
        imports {
            mavenBom 'org.springframework.boot:spring-boot-dependencies:3.5.5'
        }
    }

    dependencies {
        // Dependencias comunes para todos los módulos
        testImplementation 'org.junit.jupiter:junit-jupiter'
        testImplementation 'org.mockito:mockito-core'
        testImplementation 'org.mockito:mockito-junit-jupiter'
        testImplementation 'org.assertj:assertj-core'

        compileOnly "org.projectlombok:lombok:${lombokVersion}"
        annotationProcessor "org.projectlombok:lombok:${lombokVersion}"
    }

    test {
        useJUnitPlatform()
    }
}
```

---

## 2. MÓDULO DOMAIN (Capa de Dominio)

### 2.1 Propósito

El módulo de dominio contiene:
- **Modelos de dominio** (entidades, value objects, agregados)
- **Puertos de salida** (interfaces de repositorios)
- **Excepciones de dominio**
- **Lógica de negocio central**
- **Utilidades de dominio**

### 2.2 Estructura de Directorios

```
domain/
├── build.gradle
└── src/
    ├── main/
    │   └── java/
    │       └── com/empresa/proyecto/domain/
    │           ├── common/
    │           │   ├── exception/
    │           │   │   ├── BaseException.java
    │           │   │   ├── BusinessException.java
    │           │   │   ├── ValidationException.java
    │           │   │   ├── ValidateArgument.java
    │           │   │   ├── [Entity]NotFoundException.java
    │           │   │   └── codes/
    │           │   │       └── ExceptionCode.java
    │           │   └── utils/
    │           │       ├── CodedEnum.java
    │           │       └── EnumUtils.java
    │           └── [aggregate-name]/
    │               ├── [Entity].java
    │               ├── [ValueObject].java
    │               └── repository/
    │                   └── [Entity]Repository.java
    └── test/
        └── java/
            └── com/empresa/proyecto/domain/
```

### 2.3 Configuración build.gradle del Domain

```gradle
dependencies {
    // Solo testing - SIN dependencias externas
}
```

**IMPORTANTE**: El módulo de dominio NO debe tener dependencias de Spring, JPA, o cualquier framework externo. Es puro Java.

### 2.4 Patrón de Excepción Base

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

### 2.5 Excepción de Validación

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

### 2.6 Excepción de Negocio

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

### 2.7 Excepción Base Not Found

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

### 2.8 Códigos de Excepción Centralizados

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

### 2.9 Utilidad de Validación

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

### 2.10 Patrón de Entidad de Dominio

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

### 2.11 Patrón de Repository (Puerto de Salida)

```java
package com.empresa.proyecto.domain.[aggregate].repository;

import com.empresa.proyecto.domain.[aggregate].[Entity];
import java.util.List;
import java.util.Optional;

public interface [Entity]Repository {

    Optional<[Entity]> findById(String id);

    List<[Entity]> findAll();

    [Entity] save([Entity] entity);

    void deleteById(String id);

    // Métodos específicos del dominio
}
```

---

## 3. MÓDULO APPLICATION (Capa de Aplicación)

### 3.1 Propósito

El módulo de aplicación contiene:
- **Casos de uso** (servicios de aplicación)
- **Puertos de entrada** (interfaces de casos de uso)
- **DTOs** (objetos de transferencia de datos)
- **Mappers** (transformaciones entre dominio y DTOs)

### 3.2 Estructura de Directorios

```
application/
├── build.gradle
└── src/
    ├── main/
    │   └── java/
    │       └── com/empresa/proyecto/application/
    │           ├── dto/
    │           │   ├── [UseCase]Request.java
    │           │   └── [UseCase]Response.java
    │           ├── port/
    │           │   └── in/
    │           │       └── [UseCase].java
    │           └── service/
    │               └── [UseCase]Service.java
    └── test/
        └── java/
            └── com/empresa/proyecto/application/
```

### 3.3 Configuración build.gradle del Application

```gradle
dependencies {
    // Depende del dominio
    implementation project(':domain')

    // Spring Core (sin web, sin JPA)
    implementation 'org.springframework:spring-context'
    implementation 'org.springframework:spring-tx'

    // Validation
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Mappers (opcional)
    implementation 'org.mapstruct:mapstruct:1.6.3'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.3'

    // Testing
    testImplementation 'org.springframework:spring-test'
    testImplementation 'org.springframework.boot:spring-boot-test'
}
```

### 3.4 Patrón de Puerto de Entrada (Use Case Interface)

```java
package com.empresa.proyecto.application.port.in;

import com.empresa.proyecto.application.dto.[UseCase]Response;

public interface [UseCase] {

    [UseCase]Response execute(String param);

}
```

### 3.5 Patrón de Servicio de Aplicación

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

### 3.6 Patrón de DTOs

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

## 4. MÓDULO INFRASTRUCTURE - DRIVEN ADAPTERS (Adaptadores Secundarios)

### 4.1 Propósito

Los adaptadores driven implementan los puertos de salida del dominio. Ejemplos:
- Repositorios de bases de datos (JPA, MongoDB, etc.)
- Clientes HTTP (REST, SOAP)
- Mensajería (Kafka, RabbitMQ)
- Cache (Redis)

### 4.2 Estructura de Directorios - Ejemplo con H2/JPA

```
infrastructure/
└── driven-adapters/
    └── [adapter-name]-driven/
        ├── build.gradle
        └── src/
            ├── main/
            │   └── java/
            │       └── com/empresa/proyecto/infrastructure/[adapter]/
            │           ├── adapter/
            │           │   └── [Entity]RepositoryAdapter.java
            │           ├── entity/
            │           │   └── [Entity]Entity.java
            │           └── repository/
            │               └── Jpa[Entity]Repository.java
            └── test/
                └── java/
```

### 4.3 Configuración build.gradle del Driven Adapter (JPA)

```gradle
dependencies {
    // Domain and Application dependencies
    implementation project(':domain')
    implementation project(':application')

    // Spring Data JPA
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.data:spring-data-jpa'

    // Database (ejemplo H2)
    runtimeOnly 'com.h2database:h2'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### 4.4 Patrón de Adaptador de Repositorio

```java
package com.empresa.proyecto.infrastructure.[adapter].adapter;

import com.empresa.proyecto.domain.[aggregate].[Entity];
import com.empresa.proyecto.domain.[aggregate].repository.[Entity]Repository;
import com.empresa.proyecto.infrastructure.[adapter].entity.[Entity]Entity;
import com.empresa.proyecto.infrastructure.[adapter].repository.Jpa[Entity]Repository;
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

### 4.5 Patrón de Entidad JPA

```java
package com.empresa.proyecto.infrastructure.[adapter].entity;

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

### 4.6 Patrón de Repositorio JPA

```java
package com.empresa.proyecto.infrastructure.[adapter].repository;

import com.empresa.proyecto.infrastructure.[adapter].entity.[Entity]Entity;
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

## 5. MÓDULO INFRASTRUCTURE - DRIVER ADAPTERS (Adaptadores Primarios)

### 5.1 Propósito

Los adaptadores driver exponen la funcionalidad del sistema al exterior:
- Controladores REST
- Consumidores de mensajes
- Tareas programadas
- CLI

### 5.2 Estructura de Directorios - Ejemplo REST

```
infrastructure/
└── driver-adapters/
    └── rest-driver/
        ├── build.gradle
        └── src/
            ├── main/
            │   └── java/
            │       └── com/empresa/proyecto/infrastructure/rest/
            │           ├── controller/
            │           │   └── [Entity]Controller.java
            │           ├── exception/
            │           │   └── GlobalExceptionHandler.java
            │           └── response/
            │               ├── ApiResponse.java
            │               └── ValidationErrorResponse.java
            └── test/
                └── java/
                    └── com/empresa/proyecto/infrastructure/rest/
```

### 5.3 Configuración build.gradle del Rest Driver

```gradle
dependencies {
    // Application layer dependency
    implementation project(':application')
    implementation project(':domain')

    // Spring Web
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

### 5.4 Patrón de Controlador REST

```java
package com.empresa.proyecto.infrastructure.rest.controller;

import com.empresa.proyecto.application.dto.[UseCase]Response;
import com.empresa.proyecto.application.port.in.[UseCase];
import com.empresa.proyecto.infrastructure.rest.response.ApiResponse;
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

### 5.5 Patrón de Respuesta Estándar (ApiResponse)

```java
package com.empresa.proyecto.infrastructure.rest.response;

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

### 5.6 GlobalExceptionHandler (CRÍTICO)

```java
package com.empresa.proyecto.infrastructure.rest.exception;

import com.empresa.proyecto.domain.common.exception.BaseException;
import com.empresa.proyecto.domain.common.exception.ValidationException;
import com.empresa.proyecto.domain.common.exception.BusinessException;
import com.empresa.proyecto.domain.common.exception.codes.ExceptionCode;
import com.empresa.proyecto.domain.common.exception.BaseNotFoundException;
import com.empresa.proyecto.infrastructure.rest.response.ApiResponse;
import com.empresa.proyecto.infrastructure.rest.response.ValidationErrorResponse;

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

### 5.7 ValidationErrorResponse

```java
package com.empresa.proyecto.infrastructure.rest.response;

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

## 6. MÓDULO BOOT (Módulo de Arranque)

### 6.1 Propósito

El módulo boot:
- Ensambla todos los módulos
- Contiene la clase principal de Spring Boot
- Define la configuración de la aplicación
- Gestiona recursos (application.yml, data.sql, etc.)

### 6.2 Estructura de Directorios

```
boot/
├── build.gradle
└── src/
    └── main/
        ├── java/
        │   └── com/empresa/proyecto/
        │       └── Application.java
        └── resources/
            ├── application.yml
            ├── application-dev.yml
            ├── application-prod.yml
            ├── schema.sql (opcional)
            └── data.sql (opcional)
```

### 6.3 Configuración build.gradle del Boot

```gradle
plugins {
    id 'org.springframework.boot' version '3.5.5'
    id 'java'
}

dependencies {
    // Incluye todos los módulos
    implementation project(':domain')
    implementation project(':application')
    implementation project(':[adapter-name]-driven')
    implementation project(':[adapter-name]-driver')

    // Spring Boot
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

    // Database (ejemplo H2)
    runtimeOnly 'com.h2database:h2'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### 6.4 Clase Principal de Aplicación

```java
package com.empresa.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.empresa.proyecto")
@SpringBootConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 6.5 Configuración application.yml

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

## 7. PATRONES Y BUENAS PRÁCTICAS

### 7.1 Principios de Código Limpio

#### Inmutabilidad
- Usar campos `final` siempre que sea posible
- Evitar setters en entidades de dominio
- Usar factory methods en lugar de constructores públicos

#### Validación Centralizada
- Todas las validaciones en el dominio usando `ValidateArgument`
- Validaciones en factory methods antes de crear objetos
- Excepciones específicas para cada tipo de error

#### Separación de Responsabilidades
- Dominio: Lógica de negocio pura
- Aplicación: Orquestación de casos de uso
- Infraestructura: Detalles técnicos

#### Inyección de Dependencias
- Constructor injection (no field injection)
- Interfaces para todos los puertos
- No usar @Autowired explícito

### 7.2 Dependencias entre Capas

```
┌─────────────────┐
│      BOOT       │ ← Ensambla todo
└────────┬────────┘
         │
┌────────▼────────────────────────┐
│      INFRASTRUCTURE             │
│  ┌──────────┐    ┌──────────┐  │
│  │  Driver  │    │  Driven  │  │
│  │ Adapters │    │ Adapters │  │
│  └──────────┘    └──────────┘  │
└────────┬──────────────┬─────────┘
         │              │
         │        ┌─────▼──────┐
         │        │APPLICATION │
         │        └─────┬──────┘
         │              │
         │        ┌─────▼──────┐
         └────────►   DOMAIN   │ ← Núcleo (sin dependencias)
                  └────────────┘
```

**Reglas de Dependencia:**
1. Domain: NO depende de nadie
2. Application: Solo depende de Domain
3. Infrastructure: Depende de Domain y Application
4. Boot: Depende de todos

### 7.3 Naming Conventions

#### Paquetes
- `domain.[aggregate]` - Agregados del dominio
- `domain.[aggregate].repository` - Puertos de salida
- `domain.common.exception` - Excepciones de dominio
- `application.port.in` - Puertos de entrada
- `application.port.out` - Puertos de salida (si aplica)
- `application.service` - Implementación de casos de uso
- `application.dto` - Objetos de transferencia
- `infrastructure.[adapter].adapter` - Implementación de adaptadores
- `infrastructure.[adapter].entity` - Entidades de persistencia
- `infrastructure.[adapter].repository` - Repositorios técnicos

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

### 7.4 Testing Strategy

#### Tests Unitarios de Dominio
- Testear validaciones
- Testear comportamientos de negocio
- Sin dependencias de Spring

#### Tests Unitarios de Aplicación
- Testear casos de uso con mocks
- Verificar orquestación
- Usar Mockito

#### Tests de Integración de Infraestructura
- Testear adaptadores con base de datos real (H2)
- Testear controladores con MockMvc
- Usar @SpringBootTest

#### Tests End-to-End
- Testear flujos completos
- Usar TestRestTemplate

---

## 8. SISTEMA DE EXCEPCIONES - ARQUITECTURA COMPLETA

### 8.1 Jerarquía de Excepciones

```
RuntimeException
    └── BaseException (domain)
            ├── ValidationException (domain)
            ├── BusinessException (domain)
            └── BaseNotFoundException (domain)
                    ├── [Entity]NotFoundException (domain)
                    └── ...
```

### 8.2 Flujo de Manejo de Excepciones

```
1. Validación en Dominio
   ↓
2. Lanzamiento de Excepción Tipada
   ↓
3. Propagación a través de Capas (sin try-catch)
   ↓
4. Captura en GlobalExceptionHandler
   ↓
5. Conversión a ApiResponse con HTTP Status
   ↓
6. Respuesta JSON Consistente al Cliente
```

### 8.3 Formato de Respuesta de Error

```json
{
  "success": false,
  "errorCode": "ERR-601",
  "message": "El ítem solicitado no existe",
  "description": "Item no encontrado: MLA123456789",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 8.4 Mapeo HTTP Status

| Excepción | HTTP Status | Uso |
|-----------|-------------|-----|
| ValidationException | 400 BAD_REQUEST | Datos inválidos |
| BusinessException | 409 CONFLICT | Reglas de negocio violadas |
| BaseNotFoundException | 404 NOT_FOUND | Recurso no encontrado |
| BaseException | 500 INTERNAL_SERVER_ERROR | Error genérico |
| IllegalArgumentException | 400 BAD_REQUEST | Argumento inválido |
| Exception | 500 INTERNAL_SERVER_ERROR | Error no controlado |

---

## 9. CONFIGURACIÓN DE BASE DE DATOS

### 9.1 Schema.sql (Definición de Estructura)

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

### 9.2 Data.sql (Datos Iniciales)

```sql
-- Insertar datos de prueba
INSERT INTO [table_name] (id, name, status, created_date) VALUES
('ID001', 'Ejemplo 1', 'ACTIVE', CURRENT_TIMESTAMP),
('ID002', 'Ejemplo 2', 'ACTIVE', CURRENT_TIMESTAMP);
```

---

## 10. CHECKLIST DE IMPLEMENTACIÓN

### Creación de Nuevo Proyecto

- [ ] Crear estructura multi-módulo Gradle
- [ ] Configurar settings.gradle con todos los módulos
- [ ] Configurar build.gradle principal con versiones
- [ ] Crear módulo domain con excepciones base
- [ ] Implementar ExceptionCode completo
- [ ] Crear ValidateArgument utility
- [ ] Crear módulo application
- [ ] Crear módulo infrastructure/driven-adapters
- [ ] Crear módulo infrastructure/driver-adapters
- [ ] Implementar GlobalExceptionHandler
- [ ] Implementar ApiResponse
- [ ] Crear módulo boot
- [ ] Configurar application.yml
- [ ] Verificar dependencias entre módulos

### Añadir Nuevo Agregado

- [ ] Crear paquete domain.[aggregate]
- [ ] Crear entidades de dominio con validaciones
- [ ] Crear value objects necesarios
- [ ] Crear excepción [Entity]NotFoundException
- [ ] Crear interfaz [Entity]Repository
- [ ] Crear DTOs en application/dto
- [ ] Crear casos de uso en application/port/in
- [ ] Implementar servicios en application/service
- [ ] Crear entidades JPA en infrastructure
- [ ] Implementar JPA repository
- [ ] Implementar adapter de repositorio
- [ ] Crear controlador REST
- [ ] Escribir tests unitarios
- [ ] Escribir tests de integración

### Añadir Nuevo Endpoint

- [ ] Definir caso de uso (interfaz)
- [ ] Crear DTOs (Request/Response)
- [ ] Implementar servicio
- [ ] Crear método en controlador
- [ ] Manejar excepciones específicas
- [ ] Escribir tests

---

## 11. EJEMPLO COMPLETO DE FLUJO

### Flujo de Consulta (Query)

```
1. HTTP Request
   GET /api/v1/items/MLA123

2. Controller (REST Driver)
   ItemController.getItemDetail("MLA123")

3. Use Case (Application)
   GetItemDetailUseCase.getItemDetail("MLA123")

4. Service (Application)
   ItemDetailService.getItemDetail("MLA123")
   - Valida parámetros
   - Llama al repositorio

5. Repository Port (Domain)
   ItemRepository.findById("MLA123")

6. Repository Adapter (Infrastructure)
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

2. Controller (REST Driver)
   ItemController.createItem(request)

3. Use Case (Application)
   CreateItemUseCase.execute(request)

4. Service (Application)
   CreateItemService.execute(request)
   - Valida datos
   - Crea entidad de dominio (factory)
   - Guarda en repositorio

5. Domain Factory
   Item.from(...)
   - Valida reglas de negocio
   - Crea objeto inmutable

6. Repository Adapter
   ItemRepositoryAdapter.save(item)
   - Convierte Domain → Entity
   - Guarda en JPA
   - Convierte Entity → Domain

7. Retorno
   Domain → Service → UseCase → Controller → HTTP Response
```

---

## 12. VENTAJAS DEL ARQUETIPO

### Mantenibilidad
- Separación clara de responsabilidades
- Cambios localizados (cambiar BD no afecta dominio)
- Código autodocumentado

### Testabilidad
- Dominio testeable sin Spring
- Mocks fáciles por interfaces
- Tests de integración aislados

### Escalabilidad
- Fácil añadir nuevos adaptadores
- Múltiples implementaciones del mismo puerto
- Preparado para microservicios

### Consistencia
- Manejo de errores unificado
- Respuestas API consistentes
- Códigos de error estandarizados

### Calidad
- Validaciones centralizadas
- Inmutabilidad por defecto
- Type-safety en compilación

---

## 13. RESUMEN EJECUTIVO

Este arquetipo implementa:

1. **Arquitectura Hexagonal** con separación en 5 módulos
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

**Este arquetipo está listo para ser usado como plantilla en futuros proyectos, manteniendo la estructura pero reemplazando la lógica de negocio específica.**