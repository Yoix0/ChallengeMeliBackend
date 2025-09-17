package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.util.Objects;

public class ItemAttribute {
    
    private final String attributeId;
    private final String name;
    private final String value;
    private final String unit;
    private final ValueType valueType;
    
    private ItemAttribute(String attributeId, String name, String value, String unit, ValueType valueType) {
        this.attributeId = attributeId;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.valueType = valueType;
    }
    
    public static ItemAttribute of(String attributeId, String name, String value, String unit, String valueType) {
        validateAttributeParameters(attributeId, name, value, valueType);
        return new ItemAttribute(
            attributeId.trim(),
            name.trim(),
            value.trim(),
            unit != null ? unit.trim() : null,
            ValueType.fromString(valueType)
        );
    }
    
    private static void validateAttributeParameters(String attributeId, String name, String value, String valueType) {
        ValidateArgument.validateStringNotNullAndNotEmpty(attributeId, "attributeId");
        ValidateArgument.validateStringNotNullAndNotEmpty(name, "name");
        ValidateArgument.validateStringNotNullAndNotEmpty(value, "value");
        ValidateArgument.validateStringNotNullAndNotEmpty(valueType, "valueType");
        
        ValidateArgument.validateLength(attributeId.length(), 1, 100, "attributeId");
        ValidateArgument.validateLength(name.length(), 1, 255, "name");
        ValidateArgument.validateLength(value.length(), 1, 500, "value");
    }
    
    public String getAttributeId() {
        return attributeId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public ValueType getValueType() {
        return valueType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemAttribute that = (ItemAttribute) o;
        return Objects.equals(attributeId, that.attributeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(attributeId);
    }
    
    @Override
    public String toString() {
        return String.format("ItemAttribute{id='%s', name='%s', value='%s'}", 
                attributeId, name, value);
    }
    
    public enum ValueType {
        STRING("string"),
        NUMBER("number"),
        BOOLEAN("boolean");
        
        private final String code;
        
        ValueType(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
        
        public static ValueType fromString(String code) {
            if (code == null) return STRING;
            
            for (ValueType type : values()) {
                if (type.code.equalsIgnoreCase(code.trim())) {
                    return type;
                }
            }
            return STRING;
        }
    }
}