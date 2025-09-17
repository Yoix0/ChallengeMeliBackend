package com.mercadolibre.challenge.infrastructure.rest.controller;

import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.TrendingResponse;
import com.mercadolibre.challenge.application.port.in.GetTrendingItemsUseCase;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trending")
@CrossOrigin(origins = "*")
public class TrendingController {

    private final GetTrendingItemsUseCase getTrendingItemsUseCase;

    public TrendingController(GetTrendingItemsUseCase getTrendingItemsUseCase) {
        this.getTrendingItemsUseCase = getTrendingItemsUseCase;
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<ApiResponse<SearchResponse>> getBestSellers(
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        SearchResponse bestSellers = getTrendingItemsUseCase.getBestSellers(limit);
        return ResponseEntity.ok(
                ApiResponse.success(bestSellers, "Best sellers retrieved successfully")
        );
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<ApiResponse<SearchResponse>> getMostViewed(
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        SearchResponse mostViewed = getTrendingItemsUseCase.getMostViewed(limit);
        return ResponseEntity.ok(
                ApiResponse.success(mostViewed, "Most viewed items retrieved successfully")
        );
    }
}

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
class CategoryTrendingController {

    private final GetTrendingItemsUseCase getTrendingItemsUseCase;

    public CategoryTrendingController(GetTrendingItemsUseCase getTrendingItemsUseCase) {
        this.getTrendingItemsUseCase = getTrendingItemsUseCase;
    }

    @GetMapping("/{categoryId}/trending")
    public ResponseEntity<ApiResponse<TrendingResponse>> getTrendingByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(name = "limit", required = false, defaultValue = "15") Integer limit
    ) {
        TrendingResponse trending = getTrendingItemsUseCase.getTrendingByCategory(categoryId, limit);
        return ResponseEntity.ok(
                ApiResponse.success(trending, "Trending items by category retrieved successfully")
        );
    }
}