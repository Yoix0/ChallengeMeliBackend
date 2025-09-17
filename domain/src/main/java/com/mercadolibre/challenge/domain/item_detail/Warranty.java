package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.util.Objects;

public class Warranty {
    
    private final String type;
    private final String time;
    private final String description;
    
    private Warranty(String type, String time, String description) {
        this.type = type;
        this.time = time;
        this.description = description;
    }
    
    public static Warranty from(String type, String time, String description) {
        return new Warranty(
            type != null ? type.trim() : null,
            time != null ? time.trim() : null,
            description != null ? description.trim() : null
        );
    }
    
    public String getType() {
        return type;
    }
    
    public String getTime() {
        return time;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean hasWarranty() {
        return type != null && !type.trim().isEmpty();
    }
    
    public boolean isOfficial() {
        return hasWarranty() && type.toLowerCase().contains("oficial");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warranty warranty = (Warranty) o;
        return Objects.equals(type, warranty.type) &&
               Objects.equals(time, warranty.time) &&
               Objects.equals(description, warranty.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, time, description);
    }
    
    @Override
    public String toString() {
        return String.format("Warranty{type='%s', time='%s'}", type, time);
    }
}