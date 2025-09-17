package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.Picture;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_pictures")
public class ItemPictureEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;
    
    @Column(name = "picture_id", nullable = false, length = 100)
    private String pictureId;
    
    @Column(nullable = false, length = 1000)
    private String url;
    
    @Column(name = "secure_url", nullable = false, length = 1000)
    private String secureUrl;
    
    @Column(length = 20)
    private String size;
    
    @Column(name = "max_size", length = 20)
    private String maxSize;
    
    @Column(length = 20)
    private String quality;
    
    @Column(name = "picture_order")
    private Integer pictureOrder;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public ItemPictureEntity() {}
    
    public ItemPictureEntity(String itemId, String pictureId, String url, String secureUrl,
                            String size, String maxSize, String quality, Integer pictureOrder) {
        this.itemId = itemId;
        this.pictureId = pictureId;
        this.url = url;
        this.secureUrl = secureUrl;
        this.size = size;
        this.maxSize = maxSize;
        this.quality = quality;
        this.pictureOrder = pictureOrder;
        this.createdAt = LocalDateTime.now();
    }
    
    public static ItemPictureEntity fromDomain(String itemId, Picture picture) {
        return new ItemPictureEntity(
                itemId,
                picture.getPictureId(),
                picture.getUrl(),
                picture.getSecureUrl(),
                picture.getSize(),
                picture.getMaxSize(),
                picture.getQuality(),
                picture.getOrder()
        );
    }
    
    public Picture toDomain() {
        return Picture.of(
                this.pictureId,
                this.url,
                this.secureUrl,
                this.size,
                this.maxSize,
                this.quality,
                this.pictureOrder
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
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
    public Integer getPictureOrder() { return pictureOrder; }
    public void setPictureOrder(Integer pictureOrder) { this.pictureOrder = pictureOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ItemEntity getItem() { return item; }
    public void setItem(ItemEntity item) { this.item = item; }
}