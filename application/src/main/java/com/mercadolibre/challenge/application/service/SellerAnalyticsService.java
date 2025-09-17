package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.SearchResponse;
import com.mercadolibre.challenge.application.dto.SellerAnalyticsResponse;
import com.mercadolibre.challenge.application.dto.SellerAnalyticsResponse.*;
import com.mercadolibre.challenge.application.port.in.GetItemDetailUseCase;
import com.mercadolibre.challenge.application.port.in.GetSellerAnalyticsUseCase;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerAnalyticsService implements GetSellerAnalyticsUseCase {

    private final ItemRepository itemRepository;

    public SellerAnalyticsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public SellerAnalyticsResponse getSellerProfile(Long sellerId) {
        List<Item> sellerItems = itemRepository.findBySeller(sellerId, 10, 0);

        if (sellerItems.isEmpty()) {
            throw new RuntimeException("Seller not found: " + sellerId);
        }

        Item firstItem = sellerItems.get(0);
        SellerProfileDto profile = mapToSellerProfile(firstItem);
        SellerMetricsDto metrics = calculateSellerMetrics(sellerItems, sellerId);
        List<ItemSummaryDto> recentItems = sellerItems.stream()
                .limit(5)
                .map(this::mapToItemSummary)
                .collect(Collectors.toList());

        return new SellerAnalyticsResponse(profile, metrics, recentItems);
    }

    @Override
    public SellerAnalyticsResponse getSellerReputationDetails(Long sellerId) {
        return getSellerProfile(sellerId);
    }

    @Override
    public SearchResponse getSellerItems(Long sellerId, int limit, int offset) {
        List<Item> sellerItems = itemRepository.findBySeller(sellerId, limit, offset);

        List<SearchResponse.ItemSummaryDto> items = sellerItems.stream()
                .map(this::mapToSearchItemSummary)
                .collect(Collectors.toList());

        SearchResponse.PaginationDto pagination = new SearchResponse.PaginationDto(
                items.size(), limit, offset
        );

        return new SearchResponse(items, pagination, null, "seller_items");
    }

    @Override
    public SearchResponse getTopRatedSellers(int limit) {
        List<Item> bestSellerItems = itemRepository.findBestSellers(limit * 2);

        List<SearchResponse.ItemSummaryDto> items = bestSellerItems.stream()
                .filter(item -> "5_green".equals(item.getSeller().getReputationLevel()) ||
                               "platinum".equals(item.getSeller().getPowerSellerStatus()))
                .limit(limit)
                .map(this::mapToSearchItemSummary)
                .collect(Collectors.toList());

        SearchResponse.PaginationDto pagination = new SearchResponse.PaginationDto(
                items.size(), limit, 0
        );

        return new SearchResponse(items, pagination, null, "top_sellers");
    }

    private SellerProfileDto mapToSellerProfile(Item item) {
        return new SellerProfileDto(
                item.getSeller().getId(),
                item.getSeller().getNickname(),
                item.getSeller().getPermalink(),
                item.getSeller().getRegistrationDate(),
                item.getSeller().getCountryId(),
                item.getSeller().getReputationLevel(),
                item.getSeller().getPowerSellerStatus()
        );
    }

    private SellerMetricsDto calculateSellerMetrics(List<Item> items, Long sellerId) {
        if (items.isEmpty()) {
            return new SellerMetricsDto(0, 0, 0.0, 0.0, 0.0, 0, 0.0, "N/A");
        }

        Item firstItem = items.get(0);
        Double averagePrice = items.stream()
                .mapToLong(item -> item.getPrice().getAmountInCents())
                .average()
                .orElse(0.0) / 100.0;

        String topCategory = items.stream()
                .findFirst()
                .map(item -> item.getCategory().getName())
                .orElse("N/A");

        return new SellerMetricsDto(
                firstItem.getSeller().getTransactionsCompleted(),
                firstItem.getSeller().getTransactionsCanceled(),
                firstItem.getSeller().getRatingPositive().doubleValue(),
                firstItem.getSeller().getRatingNegative().doubleValue(),
                firstItem.getSeller().getRatingNeutral().doubleValue(),
                items.size(),
                averagePrice,
                topCategory
        );
    }

    private ItemSummaryDto mapToItemSummary(Item item) {
        PriceDto price = new PriceDto(
                item.getPrice().getAmountInCents(),
                item.getPrice().getCurrency(),
                item.getPrice().getDecimals()
        );

        String thumbnailUrl = item.getMainPicture()
                .map(picture -> picture.getUrl())
                .orElse(null);

        return new ItemSummaryDto(
                item.getId(),
                item.getTitle(),
                price,
                item.getConditionType(),
                item.getSoldQuantity(),
                thumbnailUrl
        );
    }

    private SearchResponse.ItemSummaryDto mapToSearchItemSummary(Item item) {
        SearchResponse.PriceDto price = new SearchResponse.PriceDto(
                item.getPrice().getAmountInCents(),
                item.getPrice().getCurrency(),
                item.getPrice().getDecimals()
        );

        SearchResponse.SellerSummaryDto seller = new SearchResponse.SellerSummaryDto(
                item.getSeller().getId(),
                item.getSeller().getNickname(),
                item.getSeller().getReputationLevel()
        );

        String thumbnailUrl = item.getMainPicture()
                .map(picture -> picture.getUrl())
                .orElse(null);

        return new SearchResponse.ItemSummaryDto(
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