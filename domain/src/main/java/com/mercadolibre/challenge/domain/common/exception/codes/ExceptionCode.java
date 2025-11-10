package com.mercadolibre.challenge.domain.common.exception.codes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionCode {
    // Validación de datos
    public static final String NULL_FIELD = "ERR-001";
    public static final String EMPTY_FIELD = "ERR-002";
    public static final String INVALID_VALUE = "ERR-003";
    public static final String INVALID_FORMAT = "ERR-004";
    public static final String INVALID_LENGTH = "ERR-005";
    public static final String INVALID_RANGE = "ERR-006";
    public static final String VALIDATION_ERROR = "ERR-007";

    // Autenticación y autorización
    public static final String UNAUTHORIZED = "ERR-101";
    public static final String FORBIDDEN = "ERR-102";
    public static final String INVALID_CREDENTIALS = "ERR-103";
    public static final String TOKEN_EXPIRED = "ERR-104";
    public static final String TOKEN_INVALID = "ERR-105";

    // Recursos y entidades
    public static final String RESOURCE_NOT_FOUND = "ERR-201";
    public static final String RESOURCE_ALREADY_EXISTS = "ERR-202";
    public static final String RESOURCE_CONFLICT = "ERR-203";
    public static final String RESOURCE_LOCKED = "ERR-204";

    // Usuario específico
    public static final String USER_NOT_FOUND = "ERR-301";
    public static final String USER_ALREADY_EXISTS = "ERR-302";
    public static final String EMAIL_ALREADY_EXISTS = "ERR-303";
    public static final String USERNAME_ALREADY_EXISTS = "ERR-304";
    public static final String USER_INACTIVE = "ERR-305";
    public static final String USER_BLOCKED = "ERR-306";

    // Base de datos
    public static final String DATABASE_ERROR = "ERR-401";
    public static final String CONNECTION_ERROR = "ERR-402";
    public static final String TRANSACTION_FAILED = "ERR-403";
    public static final String CONSTRAINT_VIOLATION = "ERR-404";

    // Servicios externos
    public static final String EXTERNAL_SERVICE_ERROR = "ERR-501";
    public static final String SERVICE_UNAVAILABLE = "ERR-502";
    public static final String TIMEOUT_ERROR = "ERR-503";
    public static final String API_LIMIT_EXCEEDED = "ERR-504";


    // Sistema
    public static final String INTERNAL_SERVER_ERROR = "ERR-901";
    public static final String CONFIGURATION_ERROR = "ERR-902";
    public static final String FILE_NOT_FOUND = "ERR-903";
    public static final String FILE_READ_ERROR = "ERR-904";
    public static final String SERIALIZATION_ERROR = "ERR-905";

    // Operaciones
    public static final String OPERATION_NOT_ALLOWED = "ERR-801";
    public static final String OPERATION_FAILED = "ERR-802";
    public static final String CONCURRENT_MODIFICATION = "ERR-803";
    public static final String RATE_LIMIT_EXCEEDED = "ERR-804";

    // Item específico
    public static final String ITEM_NOT_FOUND = "ERR-601";
    public static final String ITEM_NOT_ACTIVE = "ERR-602";
    public static final String ITEM_OUT_OF_STOCK = "ERR-603";
    public static final String ITEM_INVALID_PRICE = "ERR-604";

    // Reglas de negocio
    public static final String BUSINESS_RULE_VIOLATION = "ERR-701";
}