package com.mercadolibre.challenge.application.service;

import com.mercadolibre.challenge.application.dto.ComparisonResponse;
import com.mercadolibre.challenge.application.dto.ComparisonResponse.*;
import com.mercadolibre.challenge.application.port.in.CompareItemsUseCase;
import com.mercadolibre.challenge.domain.item_detail.Item;
import com.mercadolibre.challenge.domain.item_detail.ItemAttribute;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemComparisonService implements CompareItemsUseCase {

    private final ItemRepository itemRepository;

    public ItemComparisonService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ComparisonResponse compareItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("Item IDs list cannot be empty");
        }

        List<Item> items = itemIds.stream()
                .map(id -> itemRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new RuntimeException("No valid items found for comparison");
        }

        List<ComparedItemDto> comparedItems = items.stream()
                .map(this::mapToComparedItem)
                .collect(Collectors.toList());

        ComparisonSummaryDto summary = buildComparisonSummary(items);
        List<AttributeComparisonDto> attributeComparisons = buildAttributeComparisons(items);

        return new ComparisonResponse(comparedItems, summary, attributeComparisons);
    }

    private ComparedItemDto mapToComparedItem(Item item) {
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

        Map<String, String> keyAttributes = extractKeyAttributes(item);
        Double rating = calculateItemRating(item);

        return new ComparedItemDto(
                item.getId(),
                item.getTitle(),
                price,
                item.getConditionType(),
                thumbnailUrl,
                item.hasFreeShipping(),
                seller,
                item.getSoldQuantity(),
                rating,
                keyAttributes
        );
    }

    private Map<String, String> extractKeyAttributes(Item item) {
        Map<String, String> keyAttributes = new HashMap<>();

        item.getAttributes().forEach(attr -> {
            switch (attr.getAttributeId()) {
                case "BRAND":
                    keyAttributes.put("Marca", attr.getValue());
                    break;
                case "MODEL":
                    keyAttributes.put("Modelo", attr.getValue());
                    break;
                case "STORAGE":
                    keyAttributes.put("Almacenamiento", attr.getValue() + " " +
                            (attr.getUnit() != null ? attr.getUnit() : ""));
                    break;
                case "COLOR":
                    keyAttributes.put("Color", attr.getValue());
                    break;
                case "SCREEN_SIZE":
                    keyAttributes.put("Pantalla", attr.getValue() + " " +
                            (attr.getUnit() != null ? attr.getUnit() : ""));
                    break;
            }
        });

        return keyAttributes;
    }

    private Double calculateItemRating(Item item) {
        return item.getSeller().getRatingPositive().doubleValue();
    }

    private ComparisonSummaryDto buildComparisonSummary(List<Item> items) {
        Long minPrice = items.stream()
                .mapToLong(item -> item.getPrice().getAmountInCents())
                .min()
                .orElse(0L);

        Long maxPrice = items.stream()
                .mapToLong(item -> item.getPrice().getAmountInCents())
                .max()
                .orElse(0L);

        String mostPopular = items.stream()
                .max(Comparator.comparing(Item::getSoldQuantity))
                .map(Item::getId)
                .orElse(null);

        String bestValue = items.stream()
                .filter(item -> item.getSoldQuantity() > 50)
                .min(Comparator.comparing(item -> item.getPrice().getAmountInCents()))
                .map(Item::getId)
                .orElse(mostPopular);

        Item cheapestItem = items.stream()
                .min(Comparator.comparing(item -> item.getPrice().getAmountInCents()))
                .orElse(items.get(0));

        Item expensiveItem = items.stream()
                .max(Comparator.comparing(item -> item.getPrice().getAmountInCents()))
                .orElse(items.get(0));

        PriceDto cheapest = new PriceDto(
                cheapestItem.getPrice().getAmountInCents(),
                cheapestItem.getPrice().getCurrency(),
                cheapestItem.getPrice().getDecimals()
        );

        PriceDto mostExpensive = new PriceDto(
                expensiveItem.getPrice().getAmountInCents(),
                expensiveItem.getPrice().getCurrency(),
                expensiveItem.getPrice().getDecimals()
        );

        return new ComparisonSummaryDto(
                cheapest,
                mostExpensive,
                bestValue,
                mostPopular,
                items.size()
        );
    }

    private List<AttributeComparisonDto> buildAttributeComparisons(List<Item> items) {
        Map<String, List<AttributeComparisonDto.AttributeValueDto>> attributeMap = new HashMap<>();

        items.forEach(item -> {
            item.getAttributes().forEach(attr -> {
                String attributeName = getAttributeDisplayName(attr.getAttributeId());
                attributeMap.computeIfAbsent(attributeName, k -> new ArrayList<>())
                        .add(new AttributeComparisonDto.AttributeValueDto(
                                item.getId(),
                                attr.getValue(),
                                attr.getUnit()
                        ));
            });
        });

        return attributeMap.entrySet().stream()
                .map(entry -> new AttributeComparisonDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private String getAttributeDisplayName(String attributeId) {
        return switch (attributeId) {
            case "BRAND" -> "Marca";
            case "MODEL" -> "Modelo";
            case "STORAGE" -> "Almacenamiento";
            case "COLOR" -> "Color";
            case "SCREEN_SIZE" -> "Tamaño de Pantalla";
            case "RAM" -> "Memoria RAM";
            case "PROCESSOR" -> "Procesador";
            case "CONNECTION" -> "Conexión";
            case "BATTERY" -> "Batería";
            case "RESOLUTION" -> "Resolución";
            case "POWER" -> "Potencia";
            case "TYPE" -> "Tipo";
            case "FEATURES" -> "Características";
            case "GENDER" -> "Género";
            case "MATERIAL" -> "Material";
            case "WHEEL_SIZE" -> "Rodado";
            case "SPEEDS" -> "Velocidades";
            case "FRAME" -> "Cuadro";
            case "CAPACITY" -> "Capacidad";
            default -> attributeId;
        };
    }
}