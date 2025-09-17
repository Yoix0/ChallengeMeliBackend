package com.mercadolibre.challenge.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ItemDetailResponse {
    
    private String id;
    private String title;
    private PriceDto price;
    private String conditionType;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private String permalink;
    private String status;
    private String description;
    private String listingType;
    private String buyingMode;
    private Boolean freeShipping;
    private Boolean localPickUp;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    private CategoryDto category;
    private SellerDto seller;
    private List<AttributeDto> attributes;
    private List<PictureDto> pictures;
    private List<ShippingMethodDto> shippingMethods;
    private PaymentMethodDto paymentMethod;
    private WarrantyDto warranty;
    
    public ItemDetailResponse() {}
    
    public static class PriceDto {
        private BigDecimal amount;
        private String currency;
        private Integer decimals;
        
        public PriceDto() {}
        
        public PriceDto(BigDecimal amount, String currency, Integer decimals) {
            this.amount = amount;
            this.currency = currency;
            this.decimals = decimals;
        }
        
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public Integer getDecimals() { return decimals; }
        public void setDecimals(Integer decimals) { this.decimals = decimals; }
    }
    
    public static class CategoryDto {
        private String id;
        private String name;
        private String pathFromRoot;
        
        public CategoryDto() {}
        
        public CategoryDto(String id, String name, String pathFromRoot) {
            this.id = id;
            this.name = name;
            this.pathFromRoot = pathFromRoot;
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPathFromRoot() { return pathFromRoot; }
        public void setPathFromRoot(String pathFromRoot) { this.pathFromRoot = pathFromRoot; }
    }
    
    public static class SellerDto {
        private Long id;
        private String nickname;
        private String permalink;
        private LocalDateTime registrationDate;
        private String countryId;
        private String reputationLevel;
        private String powerSellerStatus;
        private Integer transactionsCompleted;
        private Integer transactionsCanceled;
        private BigDecimal ratingPositive;
        private BigDecimal ratingNegative;
        private BigDecimal ratingNeutral;
        
        public SellerDto() {}
        
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
        public Integer getTransactionsCompleted() { return transactionsCompleted; }
        public void setTransactionsCompleted(Integer transactionsCompleted) { this.transactionsCompleted = transactionsCompleted; }
        public Integer getTransactionsCanceled() { return transactionsCanceled; }
        public void setTransactionsCanceled(Integer transactionsCanceled) { this.transactionsCanceled = transactionsCanceled; }
        public BigDecimal getRatingPositive() { return ratingPositive; }
        public void setRatingPositive(BigDecimal ratingPositive) { this.ratingPositive = ratingPositive; }
        public BigDecimal getRatingNegative() { return ratingNegative; }
        public void setRatingNegative(BigDecimal ratingNegative) { this.ratingNegative = ratingNegative; }
        public BigDecimal getRatingNeutral() { return ratingNeutral; }
        public void setRatingNeutral(BigDecimal ratingNeutral) { this.ratingNeutral = ratingNeutral; }
    }
    
    public static class AttributeDto {
        private String attributeId;
        private String name;
        private String value;
        private String unit;
        private String valueType;
        
        public AttributeDto() {}
        
        public AttributeDto(String attributeId, String name, String value, String unit, String valueType) {
            this.attributeId = attributeId;
            this.name = name;
            this.value = value;
            this.unit = unit;
            this.valueType = valueType;
        }
        
        public String getAttributeId() { return attributeId; }
        public void setAttributeId(String attributeId) { this.attributeId = attributeId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getValueType() { return valueType; }
        public void setValueType(String valueType) { this.valueType = valueType; }
    }
    
    public static class PictureDto {
        private String pictureId;
        private String url;
        private String secureUrl;
        private String size;
        private String maxSize;
        private String quality;
        private Integer order;
        
        public PictureDto() {}
        
        public String getPictureId() { return pictureId; }
        public void setPictureId(String pictureId) { this.pictureId = pictureId; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getSecureUrl() { return secureUrl; }
        public void setSecureUrl(String secureUrl) { this.secureUrl = secureUrl; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getMaxSize() { return maxSize; }
        public void setMaxSize(String maxSize) { this.maxSize = maxSize; }
        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
    }
    
    public static class ShippingMethodDto {
        private Integer methodId;
        private String name;
        private String type;
        private PriceDto cost;
        private Boolean freeShipping;
        private Integer estimatedMinDays;
        private Integer estimatedMaxDays;
        private Boolean localPickUp;
        
        public ShippingMethodDto() {}
        
        public Integer getMethodId() { return methodId; }
        public void setMethodId(Integer methodId) { this.methodId = methodId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public PriceDto getCost() { return cost; }
        public void setCost(PriceDto cost) { this.cost = cost; }
        public Boolean getFreeShipping() { return freeShipping; }
        public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }
        public Integer getEstimatedMinDays() { return estimatedMinDays; }
        public void setEstimatedMinDays(Integer estimatedMinDays) { this.estimatedMinDays = estimatedMinDays; }
        public Integer getEstimatedMaxDays() { return estimatedMaxDays; }
        public void setEstimatedMaxDays(Integer estimatedMaxDays) { this.estimatedMaxDays = estimatedMaxDays; }
        public Boolean getLocalPickUp() { return localPickUp; }
        public void setLocalPickUp(Boolean localPickUp) { this.localPickUp = localPickUp; }
    }
    
    public static class PaymentMethodDto {
        private Integer installmentsQuantity;
        private BigDecimal installmentsRate;
        private BigDecimal installmentAmount;
        private Boolean acceptsCreditCard;
        private Boolean acceptsDebitCard;
        private Boolean acceptsMercadoPago;
        
        public PaymentMethodDto() {}
        
        public Integer getInstallmentsQuantity() { return installmentsQuantity; }
        public void setInstallmentsQuantity(Integer installmentsQuantity) { this.installmentsQuantity = installmentsQuantity; }
        public BigDecimal getInstallmentsRate() { return installmentsRate; }
        public void setInstallmentsRate(BigDecimal installmentsRate) { this.installmentsRate = installmentsRate; }
        public BigDecimal getInstallmentAmount() { return installmentAmount; }
        public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }
        public Boolean getAcceptsCreditCard() { return acceptsCreditCard; }
        public void setAcceptsCreditCard(Boolean acceptsCreditCard) { this.acceptsCreditCard = acceptsCreditCard; }
        public Boolean getAcceptsDebitCard() { return acceptsDebitCard; }
        public void setAcceptsDebitCard(Boolean acceptsDebitCard) { this.acceptsDebitCard = acceptsDebitCard; }
        public Boolean getAcceptsMercadoPago() { return acceptsMercadoPago; }
        public void setAcceptsMercadoPago(Boolean acceptsMercadoPago) { this.acceptsMercadoPago = acceptsMercadoPago; }
    }
    
    public static class WarrantyDto {
        private String type;
        private String time;
        private String description;
        
        public WarrantyDto() {}
        
        public WarrantyDto(String type, String time, String description) {
            this.type = type;
            this.time = time;
            this.description = description;
        }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    // Getters y Setters principales
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public PriceDto getPrice() { return price; }
    public void setPrice(PriceDto price) { this.price = price; }
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
    public CategoryDto getCategory() { return category; }
    public void setCategory(CategoryDto category) { this.category = category; }
    public SellerDto getSeller() { return seller; }
    public void setSeller(SellerDto seller) { this.seller = seller; }
    public List<AttributeDto> getAttributes() { return attributes; }
    public void setAttributes(List<AttributeDto> attributes) { this.attributes = attributes; }
    public List<PictureDto> getPictures() { return pictures; }
    public void setPictures(List<PictureDto> pictures) { this.pictures = pictures; }
    public List<ShippingMethodDto> getShippingMethods() { return shippingMethods; }
    public void setShippingMethods(List<ShippingMethodDto> shippingMethods) { this.shippingMethods = shippingMethods; }
    public PaymentMethodDto getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodDto paymentMethod) { this.paymentMethod = paymentMethod; }
    public WarrantyDto getWarranty() { return warranty; }
    public void setWarranty(WarrantyDto warranty) { this.warranty = warranty; }
}