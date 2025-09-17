package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.ComparisonResponse;

import java.util.List;

public interface CompareItemsUseCase {

    ComparisonResponse compareItems(List<String> itemIds);

}