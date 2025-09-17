package com.mercadolibre.challenge.application.dto;

import java.util.List;

public class SearchResponse {

    private List<ItemSummaryDto> items;
    private PaginationDto pagination;
    private List<FilterDto> availableFilters;
    private String appliedSort;

    public SearchResponse() {}

    public SearchResponse(List<ItemSummaryDto> items, PaginationDto pagination,
                         List<FilterDto> availableFilters, String appliedSort) {
        this.items = items;
        this.pagination = pagination;
        this.availableFilters = availableFilters;
        this.appliedSort = appliedSort;
    }

    public static class ItemSummaryDto {
        private String id;
        private String title;
        private PriceDto price;
        private String condition;
        private String thumbnailUrl;
        private Boolean freeShipping;
        private SellerSummaryDto seller;
        private Integer soldQuantity;

        public ItemSummaryDto() {}

        public ItemSummaryDto(String id, String title, PriceDto price, String condition,
                             String thumbnailUrl, Boolean freeShipping, SellerSummaryDto seller,
                             Integer soldQuantity) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.condition = condition;
            this.thumbnailUrl = thumbnailUrl;
            this.freeShipping = freeShipping;
            this.seller = seller;
            this.soldQuantity = soldQuantity;
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

    public static class PaginationDto {
        private Integer total;
        private Integer limit;
        private Integer offset;
        private Boolean hasNext;
        private Boolean hasPrevious;

        public PaginationDto() {}

        public PaginationDto(Integer total, Integer limit, Integer offset) {
            this.total = total;
            this.limit = limit;
            this.offset = offset;
            this.hasNext = (offset + limit) < total;
            this.hasPrevious = offset > 0;
        }

        public Integer getTotal() { return total; }
        public void setTotal(Integer total) { this.total = total; }

        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }

        public Integer getOffset() { return offset; }
        public void setOffset(Integer offset) { this.offset = offset; }

        public Boolean getHasNext() { return hasNext; }
        public void setHasNext(Boolean hasNext) { this.hasNext = hasNext; }

        public Boolean getHasPrevious() { return hasPrevious; }
        public void setHasPrevious(Boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    }

    public static class FilterDto {
        private String id;
        private String name;
        private String type;
        private List<FilterValueDto> values;

        public FilterDto() {}

        public FilterDto(String id, String name, String type, List<FilterValueDto> values) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.values = values;
        }

        public static class FilterValueDto {
            private String id;
            private String name;
            private Integer count;

            public FilterValueDto() {}

            public FilterValueDto(String id, String name, Integer count) {
                this.id = id;
                this.name = name;
                this.count = count;
            }

            public String getId() { return id; }
            public void setId(String id) { this.id = id; }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public Integer getCount() { return count; }
            public void setCount(Integer count) { this.count = count; }
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<FilterValueDto> getValues() { return values; }
        public void setValues(List<FilterValueDto> values) { this.values = values; }
    }

    // Main class getters and setters
    public List<ItemSummaryDto> getItems() { return items; }
    public void setItems(List<ItemSummaryDto> items) { this.items = items; }

    public PaginationDto getPagination() { return pagination; }
    public void setPagination(PaginationDto pagination) { this.pagination = pagination; }

    public List<FilterDto> getAvailableFilters() { return availableFilters; }
    public void setAvailableFilters(List<FilterDto> availableFilters) { this.availableFilters = availableFilters; }

    public String getAppliedSort() { return appliedSort; }
    public void setAppliedSort(String appliedSort) { this.appliedSort = appliedSort; }
}