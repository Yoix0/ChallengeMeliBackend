package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.TrendingResponse;
import com.mercadolibre.challenge.application.dto.SearchResponse;

public interface GetTrendingItemsUseCase {

    TrendingResponse getTrendingByCategory(String categoryId, int limit);

    SearchResponse getBestSellers(int limit);

    SearchResponse getMostViewed(int limit);

}