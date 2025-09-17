package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
public class ItemEntity {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(name = "price_amount", nullable = false)
    private Long priceAmount;
    
    @Column(name = "price_currency", length = 10)
    private String priceCurrency;
    
    @Column(name = "price_decimals")
    private Integer priceDecimals;
    
    @Column(name = "condition_type", nullable = false, length = 20)
    private String conditionType;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Column(name = "sold_quantity")
    private Integer soldQuantity;
    
    @Column(length = 500)
    private String permalink;
    
    @Column(length = 20)
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "listing_type", length = 50)
    private String listingType;
    
    @Column(name = "buying_mode", length = 50)
    private String buyingMode;
    
    @Column(name = "free_shipping")
    private Boolean freeShipping;
    
    @Column(name = "local_pick_up")
    private Boolean localPickUp;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(name = "category_id", length = 50)
    private String categoryId;
    
    @Column(name = "seller_id")
    private Long sellerId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    private SellerEntity seller;
    
    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    private List<ItemAttributeEntity> attributes;
    
    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    @OrderBy("pictureOrder ASC")
    private List<ItemPictureEntity> pictures;
    
    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    private List<ShippingMethodEntity> shippingMethods;
    
    
    public ItemEntity() {}
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getPriceAmount() { return priceAmount; }
    public void setPriceAmount(Long priceAmount) { this.priceAmount = priceAmount; }
    public String getPriceCurrency() { return priceCurrency; }
    public void setPriceCurrency(String priceCurrency) { this.priceCurrency = priceCurrency; }
    public Integer getPriceDecimals() { return priceDecimals; }
    public void setPriceDecimals(Integer priceDecimals) { this.priceDecimals = priceDecimals; }
    public String getConditionType() { return conditionType; }
    public void setConditionType(String conditionType) { this.conditionType = conditionType; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }
    public String getPermalink() { return permalink; }
    public void setPermalink(String permalink) { this.permalink = permalink; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }
    public String getBuyingMode() { return buyingMode; }
    public void setBuyingMode(String buyingMode) { this.buyingMode = buyingMode; }
    public Boolean getFreeShipping() { return freeShipping; }
    public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }
    public Boolean getLocalPickUp() { return localPickUp; }
    public void setLocalPickUp(Boolean localPickUp) { this.localPickUp = localPickUp; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public CategoryEntity getCategory() { return category; }
    public void setCategory(CategoryEntity category) { this.category = category; }
    public SellerEntity getSeller() { return seller; }
    public void setSeller(SellerEntity seller) { this.seller = seller; }
    public List<ItemAttributeEntity> getAttributes() { return attributes; }
    public void setAttributes(List<ItemAttributeEntity> attributes) { this.attributes = attributes; }
    public List<ItemPictureEntity> getPictures() { return pictures; }
    public void setPictures(List<ItemPictureEntity> pictures) { this.pictures = pictures; }
    public List<ShippingMethodEntity> getShippingMethods() { return shippingMethods; }
    public void setShippingMethods(List<ShippingMethodEntity> shippingMethods) { this.shippingMethods = shippingMethods; }
}