package com.mercadolibre.challenge.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SellerAnalyticsResponse {

    private SellerProfileDto seller;
    private SellerMetricsDto metrics;
    private List<ItemSummaryDto> recentItems;

    public SellerAnalyticsResponse() {}

    public SellerAnalyticsResponse(SellerProfileDto seller, SellerMetricsDto metrics,
                                  List<ItemSummaryDto> recentItems) {
        this.seller = seller;
        this.metrics = metrics;
        this.recentItems = recentItems;
    }

    public static class SellerProfileDto {
        private Long id;
        private String nickname;
        private String permalink;
        private LocalDateTime registrationDate;
        private String countryId;
        private String reputationLevel;
        private String powerSellerStatus;

        public SellerProfileDto() {}

        public SellerProfileDto(Long id, String nickname, String permalink, LocalDateTime registrationDate,
                               String countryId, String reputationLevel, String powerSellerStatus) {
            this.id = id;
            this.nickname = nickname;
            this.permalink = permalink;
            this.registrationDate = registrationDate;
            this.countryId = countryId;
            this.reputationLevel = reputationLevel;
            this.powerSellerStatus = powerSellerStatus;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getPermalink() { return permalink; }
        public void setPermalink(String permalink) { this.permalink = permalink; }

        public LocalDateTime getRegistrationDate() { return registrationDate; }
        public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

        public String getCountryId() { return countryId; }
        public void setCountryId(String countryId) { this.countryId = countryId; }

        public String getReputationLevel() { return reputationLevel; }
        public void setReputationLevel(String reputationLevel) { this.reputationLevel = reputationLevel; }

        public String getPowerSellerStatus() { return powerSellerStatus; }
        public void setPowerSellerStatus(String powerSellerStatus) { this.powerSellerStatus = powerSellerStatus; }
    }

    public static class SellerMetricsDto {
        private Integer transactionsCompleted;
        private Integer transactionsCanceled;
        private Double ratingPositive;
        private Double ratingNegative;
        private Double ratingNeutral;
        private Integer activeItems;
        private Double averagePrice;
        private String topCategory;

        public SellerMetricsDto() {}

        public SellerMetricsDto(Integer transactionsCompleted, Integer transactionsCanceled,
                               Double ratingPositive, Double ratingNegative, Double ratingNeutral,
                               Integer activeItems, Double averagePrice, String topCategory) {
            this.transactionsCompleted = transactionsCompleted;
            this.transactionsCanceled = transactionsCanceled;
            this.ratingPositive = ratingPositive;
            this.ratingNegative = ratingNegative;
            this.ratingNeutral = ratingNeutral;
            this.activeItems = activeItems;
            this.averagePrice = averagePrice;
            this.topCategory = topCategory;
        }

        // Getters and Setters
        public Integer getTransactionsCompleted() { return transactionsCompleted; }
        public void setTransactionsCompleted(Integer transactionsCompleted) { this.transactionsCompleted = transactionsCompleted; }

        public Integer getTransactionsCanceled() { return transactionsCanceled; }
        public void setTransactionsCanceled(Integer transactionsCanceled) { this.transactionsCanceled = transactionsCanceled; }

        public Double getRatingPositive() { return ratingPositive; }
        public void setRatingPositive(Double ratingPositive) { this.ratingPositive = ratingPositive; }

        public Double getRatingNegative() { return ratingNegative; }
        public void setRatingNegative(Double ratingNegative) { this.ratingNegative = ratingNegative; }

        public Double getRatingNeutral() { return ratingNeutral; }
        public void setRatingNeutral(Double ratingNeutral) { this.ratingNeutral = ratingNeutral; }

        public Integer getActiveItems() { return activeItems; }
        public void setActiveItems(Integer activeItems) { this.activeItems = activeItems; }

        public Double getAveragePrice() { return averagePrice; }
        public void setAveragePrice(Double averagePrice) { this.averagePrice = averagePrice; }

        public String getTopCategory() { return topCategory; }
        public void setTopCategory(String topCategory) { this.topCategory = topCategory; }
    }

    public static class ItemSummaryDto {
        private String id;
        private String title;
        private PriceDto price;
        private String condition;
        private Integer soldQuantity;
        private String thumbnailUrl;

        public ItemSummaryDto() {}

        public ItemSummaryDto(String id, String title, PriceDto price, String condition,
                             Integer soldQuantity, String thumbnailUrl) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.condition = condition;
            this.soldQuantity = soldQuantity;
            this.thumbnailUrl = thumbnailUrl;
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

        public Integer getSoldQuantity() { return soldQuantity; }
        public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
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
    public SellerProfileDto getSeller() { return seller; }
    public void setSeller(SellerProfileDto seller) { this.seller = seller; }

    public SellerMetricsDto getMetrics() { return metrics; }
    public void setMetrics(SellerMetricsDto metrics) { this.metrics = metrics; }

    public List<ItemSummaryDto> getRecentItems() { return recentItems; }
    public void setRecentItems(List<ItemSummaryDto> recentItems) { this.recentItems = recentItems; }
}