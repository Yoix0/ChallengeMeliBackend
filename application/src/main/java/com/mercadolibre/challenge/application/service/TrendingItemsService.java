package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.TrendingResponse;
import com.mercadolibre.challenge.application.dto.TrendingResponse.TrendingItemDto;
import com.mercadolibre.challenge.application.dto.TrendingResponse.PriceDto;
import com.mercadolibre.challenge.application.dto.SearchResponse.ItemSummaryDto;
import com.mercadolibre.challenge.application.dto.SearchResponse.SellerSummaryDto;
import com.mercadolibre.challenge.application.port.in.GetTrendingItemsUseCase;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TrendingItemsService implements GetTrendingItemsUseCase {

    private final ItemRepository itemRepository;

    public TrendingItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public TrendingResponse getTrendingByCategory(String categoryId, int limit) {
        List<Item> trendingItems = itemRepository.findTrendingByCategory(categoryId, limit);

        AtomicInteger rank = new AtomicInteger(1);
        List<TrendingItemDto> trending = trendingItems.stream()
                .map(item -> mapToTrendingItem(item, rank.getAndIncrement()))
                .collect(Collectors.toList());

        return new TrendingResponse(trending, categoryId, "last_7_days");
    }

    @Override
    public SearchResponse getBestSellers(int limit) {
        List<Item> bestSellers = itemRepository.findBestSellers(limit);

        List<ItemSummaryDto> items = bestSellers.stream()
                .map(this::mapToItemSummary)
                .collect(Collectors.toList());

        SearchResponse.PaginationDto pagination = new SearchResponse.PaginationDto(
                items.size(), limit, 0
        );

        return new SearchResponse(items, pagination, null, "best_sellers");
    }

    @Override
    public SearchResponse getMostViewed(int limit) {
        List<Item> mostViewed = itemRepository.findBestSellers(limit);

        List<ItemSummaryDto> items = mostViewed.stream()
                .map(this::mapToItemSummary)
                .collect(Collectors.toList());

        SearchResponse.PaginationDto pagination = new SearchResponse.PaginationDto(
                items.size(), limit, 0
        );

        return new SearchResponse(items, pagination, null, "most_viewed");
    }

    private TrendingItemDto mapToTrendingItem(Item item, int rank) {
        PriceDto price = new PriceDto(
                item.getPrice().getAmount().longValue(),
                item.getPrice().getCurrency(),
                item.getPrice().getDecimals()
        );

        String thumbnailUrl = item.getMainPicture()
                .map(picture -> picture.getUrl())
                .orElse(null);

        String reason = rank <= 3 ? "Top seller this week" : "Trending up";

        return new TrendingItemDto(
                item.getId(),
                item.getTitle(),
                price,
                thumbnailUrl,
                item.getSoldQuantity(),
                rank,
                reason
        );
    }

    private ItemSummaryDto mapToItemSummary(Item item) {
        SearchResponse.PriceDto price = new SearchResponse.PriceDto(
                item.getPrice().getAmount().longValue(),
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
}