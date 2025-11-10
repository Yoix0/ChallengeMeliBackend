package com.mercadolibre.challenge.domain.person;

import com.mercadolibre.challenge.domain.common.utils.CodedEnum;

import java.util.Arrays;

/**
 * Estados posibles de una persona en el sistema
 */
public enum PersonStatus implements CodedEnum {
    ACTIVE("ACTIVE", "Activo"),
    INACTIVE("INACTIVE", "Inactivo"),
    SUSPENDED("SUSPENDED", "Suspendido");

    private final String code;
    private final String description;

    PersonStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PersonStatus fromCode(String code) {
        return Arrays.stream(values())
            .filter(status -> status.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid PersonStatus code: " + code));
    }
}