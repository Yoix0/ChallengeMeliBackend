package com.mercadolibre.challenge.infrastructure.rest.controller;

import com.mercadolibre.challenge.application.dto.ComparisonResponse;
import com.mercadolibre.challenge.application.dto.ItemDetailResponse;
import com.mercadolibre.challenge.application.dto.SearchRequest;
import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.port.in.CompareItemsUseCase;
import com.mercadolibre.challenge.application.port.in.GetItemDetailUseCase;
import com.mercadolibre.challenge.application.port.in.SearchItemsUseCase;
import com.mercadolibre.challenge.infrastructure.rest.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class ItemDetailController {

    private final GetItemDetailUseCase getItemDetailUseCase;
    private final SearchItemsUseCase searchItemsUseCase;
    private final CompareItemsUseCase compareItemsUseCase;

    public ItemDetailController(GetItemDetailUseCase getItemDetailUseCase,
                               SearchItemsUseCase searchItemsUseCase,
                               CompareItemsUseCase compareItemsUseCase) {
        this.getItemDetailUseCase = getItemDetailUseCase;
        this.searchItemsUseCase = searchItemsUseCase;
        this.compareItemsUseCase = compareItemsUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDetailResponse>> getItemDetail(@PathVariable("id") String id) {
        ItemDetailResponse itemDetail = getItemDetailUseCase.getItemDetail(id);
        return ResponseEntity.ok(
                ApiResponse.success(itemDetail, "Item detail retrieved successfully")
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<SearchResponse>> searchItems(
            @RequestParam(name = "query",required = false) String query,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "min_price", required = false) BigDecimal min_price,
            @RequestParam(name = "max_price", required = false) BigDecimal max_price,
            @RequestParam(name = "condition", required = false) String condition,
            @RequestParam(name = "free_shipping", required = false) Boolean free_shipping,
            @RequestParam(name = "seller_reputation", required = false) String seller_reputation,
            @RequestParam(name = "sort", required = false, defaultValue = "relevance") String sort,
            @RequestParam(name = "limit",required = false, defaultValue = "50") Integer limit,
            @RequestParam(name ="offset", required = false, defaultValue = "0") Integer offset
    ) {
        SearchRequest searchRequest = new SearchRequest(
                query, category, min_price, max_price, condition,
                free_shipping, seller_reputation, sort, limit, offset
        );

        SearchResponse searchResponse = searchItemsUseCase.searchItems(searchRequest);
        return ResponseEntity.ok(
                ApiResponse.success(searchResponse, "Search completed successfully")
        );
    }

    @GetMapping("/compare")
    public ResponseEntity<ApiResponse<ComparisonResponse>> compareItems(
            @RequestParam("ids") List<String> ids
    ) {
        ComparisonResponse comparison = compareItemsUseCase.compareItems(ids);
        return ResponseEntity.ok(
                ApiResponse.success(comparison, "Items compared successfully")
        );
    }
}