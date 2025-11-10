package com.mercadolibre.challenge.domain.person;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;
import com.mercadolibre.challenge.domain.common.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entidad de dominio Person - Aplica principios DDD
 * Representa una persona con todas sus validaciones de negocio
 */
public class Person {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{7,15}$");
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 120;

    private final Long id;
    private final String documentType;
    private final String documentNumber;
    private final String firstName;
    private final String secondName;
    private final String firstLastName;
    private final String secondLastName;
    private final LocalDate birthDate;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final String city;
    private final String country;
    private final PersonStatus status;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;

    private Person(Long id, String documentType, String documentNumber, String firstName,
                  String secondName, String firstLastName, String secondLastName,
                  LocalDate birthDate, String email, String phoneNumber, String address,
                  String city, String country, PersonStatus status,
                  LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    /**
     * Factory method para crear una nueva persona
     */
    public static Person create(String documentType, String documentNumber, String firstName,
                                String secondName, String firstLastName, String secondLastName,
                                LocalDate birthDate, String email, String phoneNumber,
                                String address, String city, String country) {
        validatePersonData(documentType, documentNumber, firstName, firstLastName,
                          birthDate, email, phoneNumber);

        return new Person(
            null,
            documentType.trim().toUpperCase(),
            documentNumber.trim(),
            capitalizeFirstLetter(firstName.trim()),
            secondName != null ? capitalizeFirstLetter(secondName.trim()) : null,
            capitalizeFirstLetter(firstLastName.trim()),
            secondLastName != null ? capitalizeFirstLetter(secondLastName.trim()) : null,
            birthDate,
            email.trim().toLowerCase(),
            phoneNumber.trim(),
            address != null ? address.trim() : null,
            city != null ? capitalizeFirstLetter(city.trim()) : null,
            country != null ? capitalizeFirstLetter(country.trim()) : null,
            PersonStatus.ACTIVE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    /**
     * Factory method para reconstruir desde persistencia
     */
    public static Person from(Long id, String documentType, String documentNumber,
                             String firstName, String secondName, String firstLastName,
                             String secondLastName, LocalDate birthDate, String email,
                             String phoneNumber, String address, String city, String country,
                             PersonStatus status, LocalDateTime createdDate, LocalDateTime updatedDate) {
        validatePersonData(documentType, documentNumber, firstName, firstLastName,
                          birthDate, email, phoneNumber);
        ValidateArgument.validateNotNull(id, "id");

        return new Person(
            id,
            documentType.trim().toUpperCase(),
            documentNumber.trim(),
            firstName.trim(),
            secondName,
            firstLastName.trim(),
            secondLastName,
            birthDate,
            email.trim().toLowerCase(),
            phoneNumber.trim(),
            address,
            city,
            country,
            status != null ? status : PersonStatus.ACTIVE,
            createdDate,
            updatedDate
        );
    }

    /**
     * Actualiza los datos de la persona
     */
    public Person update(String firstName, String secondName, String firstLastName,
                        String secondLastName, String email, String phoneNumber,
                        String address, String city, String country) {
        // Validaciones
        ValidateArgument.validateStringNotNullAndNotEmpty(firstName, "firstName");
        ValidateArgument.validateStringNotNullAndNotEmpty(firstLastName, "firstLastName");
        validateEmail(email);
        validatePhoneNumber(phoneNumber);

        return new Person(
            this.id,
            this.documentType,
            this.documentNumber,
            capitalizeFirstLetter(firstName.trim()),
            secondName != null ? capitalizeFirstLetter(secondName.trim()) : null,
            capitalizeFirstLetter(firstLastName.trim()),
            secondLastName != null ? capitalizeFirstLetter(secondLastName.trim()) : null,
            this.birthDate,
            email.trim().toLowerCase(),
            phoneNumber.trim(),
            address != null ? address.trim() : null,
            city != null ? capitalizeFirstLetter(city.trim()) : null,
            country != null ? capitalizeFirstLetter(country.trim()) : null,
            this.status,
            this.createdDate,
            LocalDateTime.now()
        );
    }

    /**
     * Inactiva la persona
     */
    public Person deactivate() {
        if (this.status == PersonStatus.INACTIVE) {
            throw ValidationException.businessRule("La persona ya está inactiva");
        }

        return new Person(
            this.id, this.documentType, this.documentNumber, this.firstName,
            this.secondName, this.firstLastName, this.secondLastName, this.birthDate,
            this.email, this.phoneNumber, this.address, this.city, this.country,
            PersonStatus.INACTIVE, this.createdDate, LocalDateTime.now()
        );
    }

    /**
     * Activa la persona
     */
    public Person activate() {
        if (this.status == PersonStatus.ACTIVE) {
            throw ValidationException.businessRule("La persona ya está activa");
        }

        return new Person(
            this.id, this.documentType, this.documentNumber, this.firstName,
            this.secondName, this.firstLastName, this.secondLastName, this.birthDate,
            this.email, this.phoneNumber, this.address, this.city, this.country,
            PersonStatus.ACTIVE, this.createdDate, LocalDateTime.now()
        );
    }

    // ==================== VALIDATIONS ====================

    private static void validatePersonData(String documentType, String documentNumber,
                                          String firstName, String firstLastName,
                                          LocalDate birthDate, String email, String phoneNumber) {
        // Validaciones de campos obligatorios
        ValidateArgument.validateStringNotNullAndNotEmpty(documentType, "documentType");
        ValidateArgument.validateStringNotNullAndNotEmpty(documentNumber, "documentNumber");
        ValidateArgument.validateStringNotNullAndNotEmpty(firstName, "firstName");
        ValidateArgument.validateStringNotNullAndNotEmpty(firstLastName, "firstLastName");
        ValidateArgument.validateNotNull(birthDate, "birthDate");

        // Validaciones de longitud
        ValidateArgument.validateLength(documentType.length(), 2, 10, "documentType");
        ValidateArgument.validateLength(documentNumber.length(), 5, 20, "documentNumber");
        ValidateArgument.validateLength(firstName.length(), 2, 50, "firstName");
        ValidateArgument.validateLength(firstLastName.length(), 2, 50, "firstLastName");

        // Validaciones de negocio
        validateDocumentNumber(documentNumber);
        validateBirthDate(birthDate);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
    }

    private static void validateDocumentNumber(String documentNumber) {
        if (!documentNumber.matches("^[A-Za-z0-9-]+$")) {
            throw ValidationException.invalidFormat("documentNumber",
                "El número de documento solo puede contener letras, números y guiones");
        }
    }

    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw ValidationException.businessRule(
                "La fecha de nacimiento no puede ser futura"
            );
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        if (age < MIN_AGE) {
            throw ValidationException.businessRule(
                String.format("La persona debe ser mayor de %d años. Edad actual: %d", MIN_AGE, age)
            );
        }

        if (age > MAX_AGE) {
            throw ValidationException.businessRule(
                String.format("La edad no puede superar %d años. Edad actual: %d", MAX_AGE, age)
            );
        }
    }

    private static void validateEmail(String email) {
        ValidateArgument.validateStringNotNullAndNotEmpty(email, "email");

        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw ValidationException.invalidFormat("email",
                "El formato del email no es válido");
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        ValidateArgument.validateStringNotNullAndNotEmpty(phoneNumber, "phoneNumber");

        String cleanPhone = phoneNumber.replaceAll("[\\s()-]", "");

        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw ValidationException.invalidFormat("phoneNumber",
                "El teléfono debe contener entre 7 y 15 dígitos");
        }
    }

    // ==================== BUSINESS LOGIC ====================

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(firstName);
        if (secondName != null && !secondName.isEmpty()) {
            fullName.append(" ").append(secondName);
        }
        fullName.append(" ").append(firstLastName);
        if (secondLastName != null && !secondLastName.isEmpty()) {
            fullName.append(" ").append(secondLastName);
        }
        return fullName.toString();
    }

    public boolean isActive() {
        return PersonStatus.ACTIVE.equals(status);
    }

    public boolean isAdult() {
        return getAge() >= MIN_AGE;
    }

    // ==================== HELPERS ====================

    private static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    // ==================== GETTERS ====================

    public Long getId() {
        return id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    // ==================== EQUALS & HASHCODE ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(documentType, person.documentType) &&
               Objects.equals(documentNumber, person.documentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentType, documentNumber);
    }

    @Override
    public String toString() {
        return String.format("Person{id=%d, fullName='%s', document='%s-%s', email='%s', status=%s}",
            id, getFullName(), documentType, documentNumber, email, status);
    }
}