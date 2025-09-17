package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.ShippingMethod;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_methods")
public class ShippingMethodEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;
    
    @Column(name = "method_id", nullable = false)
    private Integer methodId;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    @Column
    private Long cost;
    
    @Column(length = 10)
    private String currency;
    
    @Column(name = "free_shipping")
    private Boolean freeShipping;
    
    @Column(name = "estimated_min_days")
    private Integer estimatedMinDays;
    
    @Column(name = "estimated_max_days")
    private Integer estimatedMaxDays;
    
    @Column(name = "local_pick_up")
    private Boolean localPickUp;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ShippingMethodEntity() {}
    
    public ShippingMethodEntity(String itemId, Integer methodId, String name, String type,
                               Long cost, String currency, Boolean freeShipping,
                               Integer estimatedMinDays, Integer estimatedMaxDays,
                               Boolean localPickUp) {
        this.itemId = itemId;
        this.methodId = methodId;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.currency = currency;
        this.freeShipping = freeShipping;
        this.estimatedMinDays = estimatedMinDays;
        this.estimatedMaxDays = estimatedMaxDays;
        this.localPickUp = localPickUp;
        this.createdAt = LocalDateTime.now();
    }
    
    public static ShippingMethodEntity fromDomain(String itemId, ShippingMethod shippingMethod) {
        return new ShippingMethodEntity(
                itemId,
                shippingMethod.getMethodId(),
                shippingMethod.getName(),
                shippingMethod.getType(),
                shippingMethod.getCost() != null ? shippingMethod.getCost().getAmountInCents() : 0L,
                shippingMethod.getCost() != null ? shippingMethod.getCost().getCurrency() : "ARS",
                shippingMethod.getFreeShipping(),
                shippingMethod.getEstimatedMinDays(),
                shippingMethod.getEstimatedMaxDays(),
                shippingMethod.getLocalPickUp()
        );
    }
    
    public ShippingMethod toDomain() {
        return ShippingMethod.of(
                this.methodId,
                this.name,
                this.type,
                this.cost,
                this.currency,
                this.freeShipping,
                this.estimatedMinDays,
                this.estimatedMaxDays,
                this.localPickUp
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public Integer getMethodId() { return methodId; }
    public void setMethodId(Integer methodId) { this.methodId = methodId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getCost() { return cost; }
    public void setCost(Long cost) { this.cost = cost; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Boolean getFreeShipping() { return freeShipping; }
    public void setFreeShipping(Boolean freeShipping) { this.freeShipping = freeShipping; }
    public Integer getEstimatedMinDays() { return estimatedMinDays; }
    public void setEstimatedMinDays(Integer estimatedMinDays) { this.estimatedMinDays = estimatedMinDays; }
    public Integer getEstimatedMaxDays() { return estimatedMaxDays; }
    public void setEstimatedMaxDays(Integer estimatedMaxDays) { this.estimatedMaxDays = estimatedMaxDays; }
    public Boolean getLocalPickUp() { return localPickUp; }
    public void setLocalPickUp(Boolean localPickUp) { this.localPickUp = localPickUp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ItemEntity getItem() { return item; }
    public void setItem(ItemEntity item) { this.item = item; }
}