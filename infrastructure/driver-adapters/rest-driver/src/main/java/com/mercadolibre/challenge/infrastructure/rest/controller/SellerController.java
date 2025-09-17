package com.mercadolibre.challenge.infrastructure.rest.controller;

import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.SellerAnalyticsResponse;
import com.mercadolibre.challenge.application.port.in.GetSellerAnalyticsUseCase;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
@CrossOrigin(origins = "*")
public class SellerController {

    private final GetSellerAnalyticsUseCase getSellerAnalyticsUseCase;

    public SellerController(GetSellerAnalyticsUseCase getSellerAnalyticsUseCase) {
        this.getSellerAnalyticsUseCase = getSellerAnalyticsUseCase;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<SellerAnalyticsResponse>> getSellerProfile(
            @PathVariable("id") Long id
    ) {
        SellerAnalyticsResponse profile = getSellerAnalyticsUseCase.getSellerProfile(id);
        return ResponseEntity.ok(
                ApiResponse.success(profile, "Seller profile retrieved successfully")
        );
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<ApiResponse<SearchResponse>> getSellerItems(
            @PathVariable("id") Long id,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(name ="offset", required = false, defaultValue = "0") Integer offset
    ) {
        SearchResponse items = getSellerAnalyticsUseCase.getSellerItems(id, limit, offset);
        return ResponseEntity.ok(
                ApiResponse.success(items, "Seller items retrieved successfully")
        );
    }

    @GetMapping("/{id}/reputation-details")
    public ResponseEntity<ApiResponse<SellerAnalyticsResponse>> getSellerReputationDetails(
            @PathVariable("id") Long id
    ) {
        SellerAnalyticsResponse reputation = getSellerAnalyticsUseCase.getSellerReputationDetails(id);
        return ResponseEntity.ok(
                ApiResponse.success(reputation, "Seller reputation details retrieved successfully")
        );
    }

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<SearchResponse>> getTopRatedSellers(
            @RequestParam(name = "limit",required = false, defaultValue = "10") Integer limit
    ) {
        SearchResponse topSellers = getSellerAnalyticsUseCase.getTopRatedSellers(limit);
        return ResponseEntity.ok(
                ApiResponse.success(topSellers, "Top rated sellers retrieved successfully")
        );
    }
}