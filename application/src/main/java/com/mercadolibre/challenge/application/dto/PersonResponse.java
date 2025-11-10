package com.mercadolibre.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String fullName;
    private LocalDate birthDate;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}