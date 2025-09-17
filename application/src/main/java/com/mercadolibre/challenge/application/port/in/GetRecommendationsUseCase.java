package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.RecommendationResponse;

public interface GetRecommendationsUseCase {

    RecommendationResponse getSimilarItems(String itemId, int limit);

    RecommendationResponse getFrequentlyBoughtTogether(String itemId, int limit);

    RecommendationResponse getAlsoViewed(String itemId, int limit);

}