package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.Category;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(name = "path_from_root", columnDefinition = "TEXT")
    private String pathFromRoot;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public CategoryEntity() {}
    
    public CategoryEntity(String id, String name, String pathFromRoot, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.pathFromRoot = pathFromRoot;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static CategoryEntity fromDomain(Category category) {
        return new CategoryEntity(
                category.getId(),
                category.getName(),
                category.getPathFromRoot(),
                null,
                null
        );
    }
    
    public Category toDomain() {
        return Category.from(this.id, this.name, this.pathFromRoot);
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPathFromRoot() { return pathFromRoot; }
    public void setPathFromRoot(String pathFromRoot) { this.pathFromRoot = pathFromRoot; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}