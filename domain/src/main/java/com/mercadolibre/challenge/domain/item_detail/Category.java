package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.util.Objects;

public class Category {
    
    private final String id;
    private final String name;
    private final String pathFromRoot;
    
    private Category(String id, String name, String pathFromRoot) {
        this.id = id;
        this.name = name;
        this.pathFromRoot = pathFromRoot;
    }
    
    public static Category from(String id, String name, String pathFromRoot) {
        validateCategoryParameters(id, name);
        
        return new Category(
            id.trim(),
            name.trim(),
            pathFromRoot != null ? pathFromRoot.trim() : null
        );
    }
    
    private static void validateCategoryParameters(String id, String name) {
        ValidateArgument.validateStringNotNullAndNotEmpty(id, "id");
        ValidateArgument.validateStringNotNullAndNotEmpty(name, "name");
        
        ValidateArgument.validateLength(id.length(), 1, 50, "id");
        ValidateArgument.validateLength(name.length(), 1, 255, "name");
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPathFromRoot() {
        return pathFromRoot;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Category{id='%s', name='%s'}", id, name);
    }
}