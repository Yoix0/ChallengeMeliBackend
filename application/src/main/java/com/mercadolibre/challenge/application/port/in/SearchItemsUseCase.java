package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.SearchRequest;
import com.mercadolibre.challenge.application.dto.SearchResponse;

public interface SearchItemsUseCase {

    SearchResponse searchItems(SearchRequest searchRequest);

}