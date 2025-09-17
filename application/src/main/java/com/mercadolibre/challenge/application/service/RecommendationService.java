package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.RecommendationResponse;
import com.mercadolibre.challenge.application.dto.RecommendationResponse.RecommendedItemDto;
import com.mercadolibre.challenge.application.dto.RecommendationResponse.PriceDto;
import com.mercadolibre.challenge.application.port.in.GetItemDetailUseCase;
import com.mercadolibre.challenge.application.port.in.GetRecommendationsUseCase;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RecommendationService implements GetRecommendationsUseCase {

    private final ItemRepository itemRepository;
    private final GetItemDetailUseCase getItemDetailUseCase;
    private final Random random = new Random();

    public RecommendationService(ItemRepository itemRepository,
                               GetItemDetailUseCase getItemDetailUseCase) {
        this.itemRepository = itemRepository;
        this.getItemDetailUseCase = getItemDetailUseCase;
    }

    @Override
    public RecommendationResponse getSimilarItems(String itemId, int limit) {
        List<Item> similarItems = itemRepository.findSimilarItems(itemId, limit);

        List<RecommendedItemDto> recommendations = similarItems.stream()
                .map(item -> mapToRecommendedItem(item, "Similar category", calculateSimilarityScore()))
                .collect(Collectors.toList());

        return new RecommendationResponse(recommendations, "similar_items", itemId);
    }

    @Override
    public RecommendationResponse getFrequentlyBoughtTogether(String itemId, int limit) {
        List<Item> relatedItems = itemRepository.findSimilarItems(itemId, limit);

        List<RecommendedItemDto> recommendations = relatedItems.stream()
                .map(item -> mapToRecommendedItem(item, "Frequently bought together", calculateFrequencyScore()))
                .collect(Collectors.toList());

        return new RecommendationResponse(recommendations, "frequently_bought_together", itemId);
    }

    @Override
    public RecommendationResponse getAlsoViewed(String itemId, int limit) {
        List<Item> bestSellers = itemRepository.findBestSellers(limit);

        List<RecommendedItemDto> recommendations = bestSellers.stream()
                .filter(item -> !item.getId().equals(itemId))
                .map(item -> mapToRecommendedItem(item, "Also viewed by other customers", calculateViewingScore()))
                .collect(Collectors.toList());

        return new RecommendationResponse(recommendations, "also_viewed", itemId);
    }

    private RecommendedItemDto mapToRecommendedItem(Item item, String reason, Double score) {
        PriceDto price = new PriceDto(
                item.getPrice().getAmountInCents(),
                item.getPrice().getCurrency(),
                item.getPrice().getDecimals()
        );

        String thumbnailUrl = item.getMainPicture()
                .map(picture -> picture.getUrl())
                .orElse(null);

        return new RecommendedItemDto(
                item.getId(),
                item.getTitle(),
                price,
                thumbnailUrl,
                item.getConditionType(),
                item.hasFreeShipping(),
                item.getSoldQuantity(),
                reason,
                score
        );
    }

    private Double calculateSimilarityScore() {
        return 0.7 + (random.nextDouble() * 0.3);
    }

    private Double calculateFrequencyScore() {
        return 0.6 + (random.nextDouble() * 0.4);
    }

    private Double calculateViewingScore() {
        return 0.5 + (random.nextDouble() * 0.5);
    }
}