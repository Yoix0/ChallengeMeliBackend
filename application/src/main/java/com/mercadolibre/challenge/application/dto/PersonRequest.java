package com.mercadolibre.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
}