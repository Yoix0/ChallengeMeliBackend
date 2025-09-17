package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.Warranty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_warranty")
public class ItemWarrantyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;
    
    @Column(name = "warranty_type", length = 100)
    private String warrantyType;
    
    @Column(name = "warranty_time", length = 50)
    private String warrantyTime;
    
    @Column(name = "warranty_description", columnDefinition = "TEXT")
    private String warrantyDescription;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ItemWarrantyEntity() {}
    
    public ItemWarrantyEntity(String itemId, String warrantyType, String warrantyTime, String warrantyDescription) {
        this.itemId = itemId;
        this.warrantyType = warrantyType;
        this.warrantyTime = warrantyTime;
        this.warrantyDescription = warrantyDescription;
        this.createdAt = LocalDateTime.now();
    }
    
    public static ItemWarrantyEntity fromDomain(String itemId, Warranty warranty) {
        return new ItemWarrantyEntity(
                itemId,
                warranty.getType(),
                warranty.getTime(),
                warranty.getDescription()
        );
    }
    
    public Warranty toDomain() {
        return Warranty.from(
                this.warrantyType,
                this.warrantyTime,
                this.warrantyDescription
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getWarrantyType() { return warrantyType; }
    public void setWarrantyType(String warrantyType) { this.warrantyType = warrantyType; }
    public String getWarrantyTime() { return warrantyTime; }
    public void setWarrantyTime(String warrantyTime) { this.warrantyTime = warrantyTime; }
    public String getWarrantyDescription() { return warrantyDescription; }
    public void setWarrantyDescription(String warrantyDescription) { this.warrantyDescription = warrantyDescription; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ItemEntity getItem() { return item; }
    public void setItem(ItemEntity item) { this.item = item; }
}