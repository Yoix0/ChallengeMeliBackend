package com.mercadolibre.challenge.domain.item_detail.repository;

import com.mercadolibre.challenge.domain.item_detail.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> findById(String id);

    List<Item> searchItems(SearchCriteria criteria);

    int countSearchResults(SearchCriteria criteria);

    List<Item> findBestSellers(int limit);

    List<Item> findTrendingByCategory(String categoryId, int limit);

    List<Item> findSimilarItems(String itemId, int limit);

    List<Item> findByCategory(String categoryId, int limit, int offset);

    List<Item> findBySeller(Long sellerId, int limit, int offset);

    List<String> getDistinctAttributeValues(String attributeId, String categoryId);

    public static class SearchCriteria {
        private String query;
        private String categoryId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String condition;
        private Boolean freeShipping;
        private String sellerReputation;
        private String sortBy;
        private String sortDirection;
        private int limit;
        private int offset;

        public SearchCriteria() {}

        public SearchCriteria(String query, String categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                              String condition, Boolean freeShipping, String sellerReputation,
                              String sortBy, String sortDirection, int limit, int offset) {
            this.query = query;
            this.categoryId = categoryId;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.condition = condition;
            this.freeShipping = freeShipping;
            this.sellerReputation = sellerReputation;
            this.sortBy = sortBy;
            this.sortDirection = sortDirection;
            this.limit = limit;
            this.offset = offset;
        }

        // Getters and Setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }

        public String getCategoryId() { return categoryId; }
        public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

        public BigDecimal getMinPrice() { return minPrice; }
        public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

        public BigDecimal getMaxPrice() { return maxPrice; }
        public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }

        public Boolean getFreeShipping() { return freeShipping; }
        public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }

        public String getSellerReputation() { return sellerReputation; }
        public void setSellerReputation(String sellerReputation) { this.sellerReputation = sellerReputation; }

        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }

        public String getSortDirection() { return sortDirection; }
        public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }

        public int getOffset() { return offset; }
        public void setOffset(int offset) { this.offset = offset; }
    }
}