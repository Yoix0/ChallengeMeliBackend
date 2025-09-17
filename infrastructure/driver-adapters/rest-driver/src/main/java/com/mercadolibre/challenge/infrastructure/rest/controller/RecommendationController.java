package com.mercadolibre.challenge.infrastructure.rest.controller;

import com.mercadolibre.challenge.application.dto.RecommendationResponse;
import com.mercadolibre.challenge.application.port.in.GetRecommendationsUseCase;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final GetRecommendationsUseCase getRecommendationsUseCase;

    public RecommendationController(GetRecommendationsUseCase getRecommendationsUseCase) {
        this.getRecommendationsUseCase = getRecommendationsUseCase;
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendations(
            @PathVariable("id") String id,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        RecommendationResponse recommendations = getRecommendationsUseCase.getSimilarItems(id, limit);
        return ResponseEntity.ok(
                ApiResponse.success(recommendations, "Recommendations retrieved successfully")
        );
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getSimilarItems(
            @PathVariable("id") String id,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        RecommendationResponse similar = getRecommendationsUseCase.getSimilarItems(id, limit);
        return ResponseEntity.ok(
                ApiResponse.success(similar, "Similar items retrieved successfully")
        );
    }

    @GetMapping("/{id}/frequently-bought-together")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getFrequentlyBoughtTogether(
            @PathVariable("id") String id,
            @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit
    ) {
        RecommendationResponse frequentlyBought = getRecommendationsUseCase.getFrequentlyBoughtTogether(id, limit);
        return ResponseEntity.ok(
                ApiResponse.success(frequentlyBought, "Frequently bought together items retrieved successfully")
        );
    }

    @GetMapping("/{id}/also-viewed")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getAlsoViewed(
            @PathVariable("id") String id,
            @RequestParam(name = "limit", required = false, defaultValue = "8") Integer limit
    ) {
        RecommendationResponse alsoViewed = getRecommendationsUseCase.getAlsoViewed(id, limit);
        return ResponseEntity.ok(
                ApiResponse.success(alsoViewed, "Also viewed items retrieved successfully")
        );
    }
}