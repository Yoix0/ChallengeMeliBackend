package com.mercadolibre.challenge.application.port.in;

import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.SellerAnalyticsResponse;

public interface GetSellerAnalyticsUseCase {

    SellerAnalyticsResponse getSellerProfile(Long sellerId);

    SellerAnalyticsResponse getSellerReputationDetails(Long sellerId);

    SearchResponse getSellerItems(Long sellerId, int limit, int offset);

    SearchResponse getTopRatedSellers(int limit);

}