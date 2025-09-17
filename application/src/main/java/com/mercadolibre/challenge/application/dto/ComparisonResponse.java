package com.mercadolibre.challenge.application.dto;

import java.util.List;
import java.util.Map;

public class ComparisonResponse {

    private List<ComparedItemDto> items;
    private ComparisonSummaryDto summary;
    private List<AttributeComparisonDto> attributeComparisons;

    public ComparisonResponse() {}

    public ComparisonResponse(List<ComparedItemDto> items, ComparisonSummaryDto summary,
                             List<AttributeComparisonDto> attributeComparisons) {
        this.items = items;
        this.summary = summary;
        this.attributeComparisons = attributeComparisons;
    }

    public static class ComparedItemDto {
        private String id;
        private String title;
        private PriceDto price;
        private String condition;
        private String thumbnailUrl;
        private Boolean freeShipping;
        private SellerSummaryDto seller;
        private Integer soldQuantity;
        private Double rating;
        private Map<String, String> keyAttributes;

        public ComparedItemDto() {}

        public ComparedItemDto(String id, String title, PriceDto price, String condition,
                              String thumbnailUrl, Boolean freeShipping, SellerSummaryDto seller,
                              Integer soldQuantity, Double rating, Map<String, String> keyAttributes) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.condition = condition;
            this.thumbnailUrl = thumbnailUrl;
            this.freeShipping = freeShipping;
            this.seller = seller;
            this.soldQuantity = soldQuantity;
            this.rating = rating;
            this.keyAttributes = keyAttributes;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public PriceDto getPrice() { return price; }
        public void setPrice(PriceDto price) { this.price = price; }

        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

        public Boolean getFreeShipping() { return freeShipping; }
        public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }

        public SellerSummaryDto getSeller() { return seller; }
        public void setSeller(SellerSummaryDto seller) { this.seller = seller; }

        public Integer getSoldQuantity() { return soldQuantity; }
        public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

        public Double getRating() { return rating; }
        public void setRating(Double rating) { this.rating = rating; }

        public Map<String, String> getKeyAttributes() { return keyAttributes; }
        public void setKeyAttributes(Map<String, String> keyAttributes) { this.keyAttributes = keyAttributes; }
    }

    public static class SellerSummaryDto {
        private Long id;
        private String nickname;
        private String reputationLevel;

        public SellerSummaryDto() {}

        public SellerSummaryDto(Long id, String nickname, String reputationLevel) {
            this.id = id;
            this.nickname = nickname;
            this.reputationLevel = reputationLevel;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getReputationLevel() { return reputationLevel; }
        public void setReputationLevel(String reputationLevel) { this.reputationLevel = reputationLevel; }
    }

    public static class PriceDto {
        private Long amount;
        private String currency;
        private Integer decimals;
        private String formatted;

        public PriceDto() {}

        public PriceDto(Long amount, String currency, Integer decimals) {
            this.amount = amount;
            this.currency = currency;
            this.decimals = decimals;
            this.formatted = formatPrice(amount, currency, decimals);
        }

        private String formatPrice(Long amount, String currency, Integer decimals) {
            if (amount == null) return null;
            double value = amount / Math.pow(10, decimals != null ? decimals : 2);
            return String.format("%s %.2f", currency, value);
        }

        public Long getAmount() { return amount; }
        public void setAmount(Long amount) { this.amount = amount; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public Integer getDecimals() { return decimals; }
        public void setDecimals(Integer decimals) { this.decimals = decimals; }

        public String getFormatted() { return formatted; }
        public void setFormatted(String formatted) { this.formatted = formatted; }
    }

    public static class ComparisonSummaryDto {
        private PriceDto cheapest;
        private PriceDto mostExpensive;
        private String bestValue;
        private String mostPopular;
        private Integer totalItems;

        public ComparisonSummaryDto() {}

        public ComparisonSummaryDto(PriceDto cheapest, PriceDto mostExpensive, String bestValue,
                                   String mostPopular, Integer totalItems) {
            this.cheapest = cheapest;
            this.mostExpensive = mostExpensive;
            this.bestValue = bestValue;
            this.mostPopular = mostPopular;
            this.totalItems = totalItems;
        }

        public PriceDto getCheapest() { return cheapest; }
        public void setCheapest(PriceDto cheapest) { this.cheapest = cheapest; }

        public PriceDto getMostExpensive() { return mostExpensive; }
        public void setMostExpensive(PriceDto mostExpensive) { this.mostExpensive = mostExpensive; }

        public String getBestValue() { return bestValue; }
        public void setBestValue(String bestValue) { this.bestValue = bestValue; }

        public String getMostPopular() { return mostPopular; }
        public void setMostPopular(String mostPopular) { this.mostPopular = mostPopular; }

        public Integer getTotalItems() { return totalItems; }
        public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }
    }

    public static class AttributeComparisonDto {
        private String attributeName;
        private List<AttributeValueDto> values;

        public AttributeComparisonDto() {}

        public AttributeComparisonDto(String attributeName, List<AttributeValueDto> values) {
            this.attributeName = attributeName;
            this.values = values;
        }

        public static class AttributeValueDto {
            private String itemId;
            private String value;
            private String unit;

            public AttributeValueDto() {}

            public AttributeValueDto(String itemId, String value, String unit) {
                this.itemId = itemId;
                this.value = value;
                this.unit = unit;
            }

            public String getItemId() { return itemId; }
            public void setItemId(String itemId) { this.itemId = itemId; }

            public String getValue() { return value; }
            public void setValue(String value) { this.value = value; }

            public String getUnit() { return unit; }
            public void setUnit(String unit) { this.unit = unit; }
        }

        public String getAttributeName() { return attributeName; }
        public void setAttributeName(String attributeName) { this.attributeName = attributeName; }

        public List<AttributeValueDto> getValues() { return values; }
        public void setValues(List<AttributeValueDto> values) { this.values = values; }
    }

    // Main class getters and setters
    public List<ComparedItemDto> getItems() { return items; }
    public void setItems(List<ComparedItemDto> items) { this.items = items; }

    public ComparisonSummaryDto getSummary() { return summary; }
    public void setSummary(ComparisonSummaryDto summary) { this.summary = summary; }

    public List<AttributeComparisonDto> getAttributeComparisons() { return attributeComparisons; }
    public void setAttributeComparisons(List<AttributeComparisonDto> attributeComparisons) { this.attributeComparisons = attributeComparisons; }
}