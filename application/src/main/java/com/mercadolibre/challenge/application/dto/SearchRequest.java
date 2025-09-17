package com.mercadolibre.challenge.application.dto;

import java.math.BigDecimal;

public class SearchRequest {

    private String query;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String condition;
    private Boolean freeShipping;
    private String sellerReputation;
    private String sort = "relevance";
    private Integer limit = 50;
    private Integer offset = 0;

    public SearchRequest() {}

    public SearchRequest(String query, String category, BigDecimal minPrice, BigDecimal maxPrice,
                        String condition, Boolean freeShipping, String sellerReputation,
                        String sort, Integer limit, Integer offset) {
        this.query = query;
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.condition = condition;
        this.freeShipping = freeShipping;
        this.sellerReputation = sellerReputation;
        this.sort = sort != null ? sort : "relevance";
        this.limit = limit != null ? limit : 50;
        this.offset = offset != null ? offset : 0;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

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

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }

    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
}