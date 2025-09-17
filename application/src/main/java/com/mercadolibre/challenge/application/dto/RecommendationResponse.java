package com.mercadolibre.challenge.application.dto;

import java.util.List;

public class RecommendationResponse {

    private List<RecommendedItemDto> recommendedItems;
    private String recommendationType;
    private String baseItemId;

    public RecommendationResponse() {}

    public RecommendationResponse(List<RecommendedItemDto> recommendedItems,
                                 String recommendationType, String baseItemId) {
        this.recommendedItems = recommendedItems;
        this.recommendationType = recommendationType;
        this.baseItemId = baseItemId;
    }

    public static class RecommendedItemDto {
        private String id;
        private String title;
        private PriceDto price;
        private String thumbnailUrl;
        private String condition;
        private Boolean freeShipping;
        private Integer soldQuantity;
        private String recommendationReason;
        private Double confidenceScore;

        public RecommendedItemDto() {}

        public RecommendedItemDto(String id, String title, PriceDto price, String thumbnailUrl,
                                 String condition, Boolean freeShipping, Integer soldQuantity,
                                 String recommendationReason, Double confidenceScore) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.thumbnailUrl = thumbnailUrl;
            this.condition = condition;
            this.freeShipping = freeShipping;
            this.soldQuantity = soldQuantity;
            this.recommendationReason = recommendationReason;
            this.confidenceScore = confidenceScore;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public PriceDto getPrice() { return price; }
        public void setPrice(PriceDto price) { this.price = price; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }

        public Boolean getFreeShipping() { return freeShipping; }
        public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }

        public Integer getSoldQuantity() { return soldQuantity; }
        public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

        public String getRecommendationReason() { return recommendationReason; }
        public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }

        public Double getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
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

    // Main class getters and setters
    public List<RecommendedItemDto> getRecommendedItems() { return recommendedItems; }
    public void setRecommendedItems(List<RecommendedItemDto> recommendedItems) { this.recommendedItems = recommendedItems; }

    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }

    public String getBaseItemId() { return baseItemId; }
    public void setBaseItemId(String baseItemId) { this.baseItemId = baseItemId; }
}