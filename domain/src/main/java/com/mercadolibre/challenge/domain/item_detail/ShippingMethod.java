package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.util.Objects;

public class ShippingMethod {
    
    private final Integer methodId;
    private final String name;
    private final String type;
    private final Price cost;
    private final Boolean freeShipping;
    private final Integer estimatedMinDays;
    private final Integer estimatedMaxDays;
    private final Boolean localPickUp;
    
    private ShippingMethod(Integer methodId, String name, String type, Price cost, 
                          Boolean freeShipping, Integer estimatedMinDays, Integer estimatedMaxDays, 
                          Boolean localPickUp) {
        this.methodId = methodId;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.freeShipping = freeShipping;
        this.estimatedMinDays = estimatedMinDays;
        this.estimatedMaxDays = estimatedMaxDays;
        this.localPickUp = localPickUp;
    }
    
    public static ShippingMethod of(Integer methodId, String name, String type, Long costAmount, 
                                  String currency, Boolean freeShipping, Integer estimatedMinDays, 
                                  Integer estimatedMaxDays, Boolean localPickUp) {
        validateShippingParameters(methodId, name, type);
        
        Price cost = Price.of(costAmount != null ? costAmount : 0L, 
                            currency != null ? currency : "ARS", 2);
        
        return new ShippingMethod(
            methodId,
            name.trim(),
            type.trim(),
            cost,
            freeShipping != null ? freeShipping : false,
            estimatedMinDays,
            estimatedMaxDays,
            localPickUp != null ? localPickUp : false
        );
    }
    
    private static void validateShippingParameters(Integer methodId, String name, String type) {
        ValidateArgument.validateNotNull(methodId, "methodId");
        ValidateArgument.validateStringNotNullAndNotEmpty(name, "name");
        ValidateArgument.validateStringNotNullAndNotEmpty(type, "type");
        
        ValidateArgument.validateLength(name.length(), 1, 255, "name");
        ValidateArgument.validateLength(type.length(), 1, 50, "type");
        
        if (methodId < 1) {
            throw new IllegalArgumentException("El ID del método debe ser mayor a 0");
        }
    }
    
    public Integer getMethodId() {
        return methodId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public Price getCost() {
        return cost;
    }
    
    public Boolean getFreeShipping() {
        return freeShipping;
    }
    
    public Integer getEstimatedMinDays() {
        return estimatedMinDays;
    }
    
    public Integer getEstimatedMaxDays() {
        return estimatedMaxDays;
    }
    
    public Boolean getLocalPickUp() {
        return localPickUp;
    }
    
    public boolean isFree() {
        return Boolean.TRUE.equals(freeShipping) || 
               (cost != null && cost.getAmountInCents() == 0);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingMethod that = (ShippingMethod) o;
        return Objects.equals(methodId, that.methodId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(methodId);
    }
    
    @Override
    public String toString() {
        return String.format("ShippingMethod{id=%d, name='%s', type='%s', free=%s}", 
                methodId, name, type, freeShipping);
    }
}