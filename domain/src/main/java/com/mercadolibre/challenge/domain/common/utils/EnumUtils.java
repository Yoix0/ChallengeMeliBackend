package com.mercadolibre.challenge.domain.common.utils;


import java.util.Arrays;

/**
 * Utilidades para trabajar con enums que implementan CodedEnum
 */
public final class EnumUtils {

    private EnumUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Busca un enum por código
     * @param enumClass clase del enum
     * @param code código a buscar
     * @param <T> tipo del enum que implementa CodedEnum
     * @return enum correspondiente
     * @throws IllegalArgumentException si no encuentra el código
     */
    public static <T extends Enum<T> & CodedEnum> T fromCode(Class<T> enumClass, String code) {
        if (code == null) {
            throw new IllegalArgumentException("El código no puede ser null");
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> enumConstant.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Código no válido para " + enumClass.getSimpleName() + ": " + code));
    }

    /**
     * Verifica si existe un código
     * @param enumClass clase del enum
     * @param code código a verificar
     * @param <T> tipo del enum que implementa CodedEnum
     * @return true si existe, false si no
     */
    public static <T extends Enum<T> & CodedEnum> boolean isValidCode(Class<T> enumClass, String code) {
        if (code == null) return false;

        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(enumConstant -> enumConstant.getCode().equalsIgnoreCase(code));
    }
}