package com.mercadolibre.challenge.infrastructure.h2.item_detail.adapter;

import com.mercadolibre.challenge.domain.item_detail.*;
import com.mercadolibre.challenge.domain.item_detail.repository.ItemRepository;
import com.mercadolibre.challenge.infrastructure.h2.item_detail.entity.ItemEntity;
import com.mercadolibre.challenge.infrastructure.h2.item_detail.repository.JpaItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryAdapter implements ItemRepository {

    private final JpaItemRepository jpaItemRepository;

    public ItemRepositoryAdapter(JpaItemRepository jpaItemRepository) {
        this.jpaItemRepository = jpaItemRepository;
    }

    @Override
    public Optional<Item> findById(String id) {
        return jpaItemRepository.findByIdWithDetails(id)
                .map(this::entityToDomain);
    }

    @Override
    public List<Item> searchItems(SearchCriteria criteria) {
        Pageable pageable = createPageable(criteria);

        Long minPriceAmount = convertToAmount(criteria.getMinPrice());
        Long maxPriceAmount = convertToAmount(criteria.getMaxPrice());

        List<ItemEntity> entities = jpaItemRepository.findItemsBySearchCriteria(
                criteria.getQuery(),
                criteria.getCategoryId(),
                minPriceAmount,
                maxPriceAmount,
                criteria.getCondition(),
                criteria.getFreeShipping(),
                criteria.getSellerReputation(),
                pageable
        );

        return entities.stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countSearchResults(SearchCriteria criteria) {
        Long minPriceAmount = convertToAmount(criteria.getMinPrice());
        Long maxPriceAmount = convertToAmount(criteria.getMaxPrice());

        return jpaItemRepository.countItemsBySearchCriteria(
                criteria.getQuery(),
                criteria.getCategoryId(),
                minPriceAmount,
                maxPriceAmount,
                criteria.getCondition(),
                criteria.getFreeShipping(),
                criteria.getSellerReputation()
        );
    }

    @Override
    public List<Item> findBestSellers(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jpaItemRepository.findBestSellers(pageable).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findTrendingByCategory(String categoryId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jpaItemRepository.findTrendingByCategory(categoryId, pageable).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findSimilarItems(String itemId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return jpaItemRepository.findSimilarItemsByCategory(itemId, pageable).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByCategory(String categoryId, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaItemRepository.findByCategory(categoryId, pageable).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findBySeller(Long sellerId, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jpaItemRepository.findBySeller(sellerId, pageable).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDistinctAttributeValues(String attributeId, String categoryId) {
        return jpaItemRepository.findDistinctAttributeValues(attributeId, categoryId);
    }

    private Pageable createPageable(SearchCriteria criteria) {
        Sort sort = createSort(criteria.getSortBy(), criteria.getSortDirection());
        return PageRequest.of(
                criteria.getOffset() / criteria.getLimit(),
                criteria.getLimit(),
                sort
        );
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return switch (sortBy != null ? sortBy.toLowerCase() : "relevance") {
            case "price" -> Sort.by(direction, "priceAmount");
            case "sold_quantity" -> Sort.by(direction, "soldQuantity");
            case "created_date" -> Sort.by(direction, "createdDate");
            default -> Sort.by(Sort.Direction.DESC, "soldQuantity", "createdDate");
        };
    }

    private Long convertToAmount(BigDecimal price) {
        if (price == null) return null;
        return price.multiply(new BigDecimal("100")).longValue();
    }
    
    private Item entityToDomain(ItemEntity entity) {
        Price price = Price.of(
                entity.getPriceAmount(),
                entity.getPriceCurrency(),
                entity.getPriceDecimals()
        );
        
        Category category = entity.getCategory() != null 
                ? entity.getCategory().toDomain() 
                : null;
        
        Seller seller = entity.getSeller() != null 
                ? entity.getSeller().toDomain() 
                : null;
        
        List<ItemAttribute> attributes = entity.getAttributes() != null
                ? entity.getAttributes().stream()
                    .map(attr -> attr.toDomain())
                    .toList()
                : new ArrayList<>();
        
        List<Picture> pictures = entity.getPictures() != null
                ? entity.getPictures().stream()
                    .map(pic -> pic.toDomain())
                    .toList()
                : new ArrayList<>();
        
        List<ShippingMethod> shippingMethods = entity.getShippingMethods() != null
                ? entity.getShippingMethods().stream()
                    .map(shipping -> shipping.toDomain())
                    .toList()
                : new ArrayList<>();
        
        PaymentMethod paymentMethod = null;

        Warranty warranty = null;
        
        return Item.from(
                entity.getId(),
                entity.getTitle(),
                price,
                entity.getConditionType(),
                entity.getAvailableQuantity(),
                entity.getSoldQuantity(),
                entity.getPermalink(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getListingType(),
                entity.getBuyingMode(),
                entity.getFreeShipping(),
                entity.getLocalPickUp(),
                entity.getCreatedDate(),
                entity.getUpdatedDate(),
                category,
                seller,
                attributes,
                pictures,
                shippingMethods,
                paymentMethod,
                warranty
        );
    }
}