package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.ItemAttribute;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_attributes")
public class ItemAttributeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;
    
    @Column(name = "attribute_id", nullable = false, length = 100)
    private String attributeId;
    
    @Column(name = "attribute_name", nullable = false, length = 255)
    private String attributeName;
    
    @Column(name = "attribute_value", nullable = false, length = 500)
    private String attributeValue;
    
    @Column(name = "attribute_unit", length = 50)
    private String attributeUnit;
    
    @Column(name = "value_type", length = 20)
    private String valueType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ItemAttributeEntity() {}
    
    public ItemAttributeEntity(String itemId, String attributeId, String attributeName,
                              String attributeValue, String attributeUnit, String valueType) {
        this.itemId = itemId;
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.attributeUnit = attributeUnit;
        this.valueType = valueType;
        this.createdAt = LocalDateTime.now();
    }
    
    public static ItemAttributeEntity fromDomain(String itemId, ItemAttribute attribute) {
        return new ItemAttributeEntity(
                itemId,
                attribute.getAttributeId(),
                attribute.getName(),
                attribute.getValue(),
                attribute.getUnit(),
                attribute.getValueType().getCode()
        );
    }
    
    public ItemAttribute toDomain() {
        return ItemAttribute.of(
                this.attributeId,
                this.attributeName,
                this.attributeValue,
                this.attributeUnit,
                this.valueType
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getAttributeId() { return attributeId; }
    public void setAttributeId(String attributeId) { this.attributeId = attributeId; }
    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName) { this.attributeName = attributeName; }
    public String getAttributeValue() { return attributeValue; }
    public void setAttributeValue(String attributeValue) { this.attributeValue = attributeValue; }
    public String getAttributeUnit() { return attributeUnit; }
    public void setAttributeUnit(String attributeUnit) { this.attributeUnit = attributeUnit; }
    public String getValueType() { return valueType; }
    public void setValueType(String valueType) { this.valueType = valueType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ItemEntity getItem() { return item; }
    public void setItem(ItemEntity item) { this.item = item; }
}