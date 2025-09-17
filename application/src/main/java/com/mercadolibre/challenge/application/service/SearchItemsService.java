package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.SearchRequest;
import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.SearchResponse.*;
import com.mercadolibre.challenge.application.port.in.SearchItemsUseCase;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository.SearchCriteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchItemsService implements SearchItemsUseCase {

    private final ItemRepository itemRepository;

    public SearchItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public SearchResponse searchItems(SearchRequest searchRequest) {
        SearchCriteria criteria = buildSearchCriteria(searchRequest);

        List<Item> items = itemRepository.searchItems(criteria);
        int totalCount = itemRepository.countSearchResults(criteria);

        List<ItemSummaryDto> itemSummaries = items.stream()
                .map(this::mapToItemSummary)
                .collect(Collectors.toList());

        PaginationDto pagination = new PaginationDto(
                totalCount,
                searchRequest.getLimit(),
                searchRequest.getOffset()
        );

        List<FilterDto> availableFilters = buildAvailableFilters(searchRequest.getCategory());

        return new SearchResponse(itemSummaries, pagination, availableFilters, searchRequest.getSort());
    }

    private SearchCriteria buildSearchCriteria(SearchRequest request) {
        String sortBy = "relevance";
        String sortDirection = "desc";

        if (request.getSort() != null) {
            switch (request.getSort().toLowerCase()) {
                case "price_asc":
                    sortBy = "price";
                    sortDirection = "asc";
                    break;
                case "price_desc":
                    sortBy = "price";
                    sortDirection = "desc";
                    break;
                case "best_sellers":
                    sortBy = "sold_quantity";
                    sortDirection = "desc";
                    break;
                case "newest":
                    sortBy = "created_date";
                    sortDirection = "desc";
                    break;
                default:
                    sortBy = "relevance";
                    sortDirection = "desc";
                    break;
            }
        }

        BigDecimal minPrice = request.getMinPrice();
        BigDecimal maxPrice = request.getMaxPrice();

        return new SearchCriteria(
                request.getQuery(),
                request.getCategory(),
                minPrice,
                maxPrice,
                request.getCondition(),
                request.getFreeShipping(),
                request.getSellerReputation(),
                sortBy,
                sortDirection,
                request.getLimit(),
                request.getOffset()
        );
    }

    private ItemSummaryDto mapToItemSummary(Item item) {
        PriceDto price = new PriceDto(
                item.getPrice().getAmountInCents(),
                item.getPrice().getCurrency(),
                item.getPrice().getDecimals()
        );

        SellerSummaryDto seller = new SellerSummaryDto(
                item.getSeller().getId(),
                item.getSeller().getNickname(),
                item.getSeller().getReputationLevel()
        );

        String thumbnailUrl = item.getMainPicture()
                .map(picture -> picture.getUrl())
                .orElse(null);

        return new ItemSummaryDto(
                item.getId(),
                item.getTitle(),
                price,
                item.getConditionType(),
                thumbnailUrl,
                item.hasFreeShipping(),
                seller,
                item.getSoldQuantity()
        );
    }

    private List<FilterDto> buildAvailableFilters(String categoryId) {
        List<FilterDto> filters = new ArrayList<>();

        filters.add(new FilterDto("condition", "Condición", "list", Arrays.asList(
                new FilterDto.FilterValueDto("new", "Nuevo", null),
                new FilterDto.FilterValueDto("used", "Usado", null)
        )));

        filters.add(new FilterDto("shipping", "Envío", "list", Arrays.asList(
                new FilterDto.FilterValueDto("free", "Gratis", null),
                new FilterDto.FilterValueDto("local_pickup", "Retiro en persona", null)
        )));

        filters.add(new FilterDto("seller_reputation", "Reputación del vendedor", "list", Arrays.asList(
                new FilterDto.FilterValueDto("5_green", "5 estrellas", null),
                new FilterDto.FilterValueDto("4_light_green", "4 estrellas", null),
                new FilterDto.FilterValueDto("3_yellow", "3 estrellas", null)
        )));

        if (categoryId != null) {
            List<String> brandValues = itemRepository.getDistinctAttributeValues("BRAND", categoryId);
            if (!brandValues.isEmpty()) {
                List<FilterDto.FilterValueDto> brandFilters = brandValues.stream()
                        .map(brand -> new FilterDto.FilterValueDto(brand.toLowerCase(), brand, null))
                        .collect(Collectors.toList());
                filters.add(new FilterDto("brand", "Marca", "list", brandFilters));
            }
        }

        return filters;
    }
}