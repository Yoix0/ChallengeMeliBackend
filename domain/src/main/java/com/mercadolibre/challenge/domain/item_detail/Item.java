package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Item {
    
    private final String id;
    private final String title;
    private final Price price;
    private final String conditionType;
    private final Integer availableQuantity;
    private final Integer soldQuantity;
    private final String permalink;
    private final String status;
    private final String description;
    private final String listingType;
    private final String buyingMode;
    private final Boolean freeShipping;
    private final Boolean localPickUp;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;
    
    private final Category category;
    private final Seller seller;
    private final List<ItemAttribute> attributes;
    private final List<Picture> pictures;
    private final List<ShippingMethod> shippingMethods;
    private final PaymentMethod paymentMethod;
    private final Warranty warranty;
    
    private Item(String id, String title, Price price, String conditionType,
                Integer availableQuantity, Integer soldQuantity, String permalink,
                String status, String description, String listingType, String buyingMode,
                Boolean freeShipping, Boolean localPickUp, LocalDateTime createdDate,
                LocalDateTime updatedDate, Category category, Seller seller,
                List<ItemAttribute> attributes, List<Picture> pictures,
                List<ShippingMethod> shippingMethods, PaymentMethod paymentMethod,
                Warranty warranty) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.conditionType = conditionType;
        this.availableQuantity = availableQuantity;
        this.soldQuantity = soldQuantity;
        this.permalink = permalink;
        this.status = status;
        this.description = description;
        this.listingType = listingType;
        this.buyingMode = buyingMode;
        this.freeShipping = freeShipping;
        this.localPickUp = localPickUp;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.category = category;
        this.seller = seller;
        this.attributes = Collections.unmodifiableList(attributes != null ? attributes : new ArrayList<>());
        this.pictures = Collections.unmodifiableList(pictures != null ? 
            pictures.stream()
                .sorted(Comparator.comparing(Picture::getOrder))
                .collect(Collectors.toList()) : new ArrayList<>());
        this.shippingMethods = Collections.unmodifiableList(shippingMethods != null ? shippingMethods : new ArrayList<>());
        this.paymentMethod = paymentMethod;
        this.warranty = warranty;
    }
    
    public static Item from(String id, String title, Price price, String conditionType,
                           Integer availableQuantity, Integer soldQuantity, String permalink,
                           String status, String description, String listingType, String buyingMode,
                           Boolean freeShipping, Boolean localPickUp, LocalDateTime createdDate,
                           LocalDateTime updatedDate, Category category, Seller seller,
                           List<ItemAttribute> attributes, List<Picture> pictures,
                           List<ShippingMethod> shippingMethods, PaymentMethod paymentMethod,
                           Warranty warranty) {
        validateItemParameters(id, title, price, conditionType);
        
        return new Item(
            id.trim(),
            title.trim(),
            price,
            conditionType.trim(),
            availableQuantity != null ? availableQuantity : 0,
            soldQuantity != null ? soldQuantity : 0,
            permalink,
            status != null ? status.trim() : "active",
            description,
            listingType,
            buyingMode,
            freeShipping != null ? freeShipping : false,
            localPickUp != null ? localPickUp : false,
            createdDate,
            updatedDate,
            category,
            seller,
            attributes,
            pictures,
            shippingMethods,
            paymentMethod,
            warranty
        );
    }
    
    private static void validateItemParameters(String id, String title, Price price, String conditionType) {
        ValidateArgument.validateStringNotNullAndNotEmpty(id, "id");
        ValidateArgument.validateStringNotNullAndNotEmpty(title, "title");
        ValidateArgument.validateNotNull(price, "price");
        ValidateArgument.validateStringNotNullAndNotEmpty(conditionType, "conditionType");
        
        ValidateArgument.validateLength(id.length(), 1, 50, "id");
        ValidateArgument.validateLength(title.length(), 1, 500, "title");
        ValidateArgument.validateLength(conditionType.length(), 1, 20, "conditionType");
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Price getPrice() {
        return price;
    }
    
    public String getConditionType() {
        return conditionType;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    
    public Integer getSoldQuantity() {
        return soldQuantity;
    }
    
    public String getPermalink() {
        return permalink;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getListingType() {
        return listingType;
    }
    
    public String getBuyingMode() {
        return buyingMode;
    }
    
    public Boolean getFreeShipping() {
        return freeShipping;
    }
    
    public Boolean getLocalPickUp() {
        return localPickUp;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public Seller getSeller() {
        return seller;
    }
    
    public List<ItemAttribute> getAttributes() {
        return attributes;
    }
    
    public List<Picture> getPictures() {
        return pictures;
    }
    
    public List<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public Warranty getWarranty() {
        return warranty;
    }
    
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    public boolean isNew() {
        return "new".equalsIgnoreCase(conditionType);
    }
    
    public boolean hasFreeShipping() {
        return Boolean.TRUE.equals(freeShipping) || 
               shippingMethods.stream().anyMatch(ShippingMethod::isFree);
    }
    
    public boolean hasLocalPickUp() {
        return Boolean.TRUE.equals(localPickUp) ||
               shippingMethods.stream().anyMatch(ShippingMethod::getLocalPickUp);
    }
    
    public Optional<Picture> getMainPicture() {
        return pictures.stream().findFirst();
    }
    
    public Optional<ItemAttribute> getAttributeById(String attributeId) {
        return attributes.stream()
                .filter(attr -> attr.getAttributeId().equals(attributeId))
                .findFirst();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Item{id='%s', title='%s', price=%s, status='%s'}", 
                id, title, price, status);
    }
}