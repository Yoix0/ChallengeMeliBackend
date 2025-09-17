package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Price {
    
    private final BigDecimal amount;
    private final String currency;
    private final Integer decimals;
    
    private Price(BigDecimal amount, String currency, Integer decimals) {
        this.amount = amount.setScale(decimals, RoundingMode.HALF_UP);
        this.currency = currency;
        this.decimals = decimals;
    }
    
    public static Price of(Long amountCents, String currency, Integer decimals) {
        validatePriceParameters(amountCents, currency, decimals);
        BigDecimal amount = BigDecimal.valueOf(amountCents, decimals);
        return new Price(amount, currency.toUpperCase().trim(), decimals);
    }
    
    public static Price of(BigDecimal amount, String currency, Integer decimals) {
        validatePriceParameters(amount, currency, decimals);
        return new Price(amount, currency.toUpperCase().trim(), decimals);
    }
    
    private static void validatePriceParameters(Object amount, String currency, Integer decimals) {
        ValidateArgument.validateNotNull(amount, "amount");
        ValidateArgument.validateStringNotNullAndNotEmpty(currency, "currency");
        ValidateArgument.validateNotNull(decimals, "decimals");
        
        if (decimals < 0 || decimals > 4) {
            throw new IllegalArgumentException("Los decimales deben estar entre 0 y 4");
        }
        
        if (currency.length() != 3) {
            throw new IllegalArgumentException("La moneda debe tener exactamente 3 caracteres");
        }
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public Integer getDecimals() {
        return decimals;
    }
    
    public Long getAmountInCents() {
        return amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals))).longValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(amount, price.amount) && 
               Objects.equals(currency, price.currency) && 
               Objects.equals(decimals, price.decimals);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency, decimals);
    }
    
    @Override
    public String toString() {
        return String.format("Price{amount=%s, currency='%s', decimals=%d}", 
                amount, currency, decimals);
    }
}