package com.mercadolibre.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonListResponse {
    private List<PersonResponse> persons;
    private int total;
    private int offset;
    private int limit;
}