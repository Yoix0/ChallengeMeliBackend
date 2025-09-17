package com.mercadolibre.challenge.application.dto;

import java.util.List;

public class TrendingResponse {

    private List<TrendingItemDto> trendingItems;
    private String category;
    private String timeFrame;

    public TrendingResponse() {}

    public TrendingResponse(List<TrendingItemDto> trendingItems, String category, String timeFrame) {
        this.trendingItems = trendingItems;
        this.category = category;
        this.timeFrame = timeFrame;
    }

    public static class TrendingItemDto {
        private String id;
        private String title;
        private PriceDto price;
        private String thumbnailUrl;
        private Integer soldQuantity;
        private Integer rank;
        private String trendingReason;

        public TrendingItemDto() {}

        public TrendingItemDto(String id, String title, PriceDto price, String thumbnailUrl,
                              Integer soldQuantity, Integer rank, String trendingReason) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.thumbnailUrl = thumbnailUrl;
            this.soldQuantity = soldQuantity;
            this.rank = rank;
            this.trendingReason = trendingReason;
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

        public Integer getSoldQuantity() { return soldQuantity; }
        public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }

        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }

        public String getTrendingReason() { return trendingReason; }
        public void setTrendingReason(String trendingReason) { this.trendingReason = trendingReason; }
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
    public List<TrendingItemDto> getTrendingItems() { return trendingItems; }
    public void setTrendingItems(List<TrendingItemDto> trendingItems) { this.trendingItems = trendingItems; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTimeFrame() { return timeFrame; }
    public void setTimeFrame(String timeFrame) { this.timeFrame = timeFrame; }
}