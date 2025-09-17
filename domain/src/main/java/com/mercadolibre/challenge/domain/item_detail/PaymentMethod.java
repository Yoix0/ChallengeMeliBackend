package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentMethod {
    
    private final Integer installmentsQuantity;
    private final BigDecimal installmentsRate;
    private final BigDecimal installmentAmount;
    private final Boolean acceptsCreditCard;
    private final Boolean acceptsDebitCard;
    private final Boolean acceptsMercadoPago;
    
    private PaymentMethod(Integer installmentsQuantity, BigDecimal installmentsRate, 
                         BigDecimal installmentAmount, Boolean acceptsCreditCard, 
                         Boolean acceptsDebitCard, Boolean acceptsMercadoPago) {
        this.installmentsQuantity = installmentsQuantity;
        this.installmentsRate = installmentsRate;
        this.installmentAmount = installmentAmount;
        this.acceptsCreditCard = acceptsCreditCard;
        this.acceptsDebitCard = acceptsDebitCard;
        this.acceptsMercadoPago = acceptsMercadoPago;
    }
    
    public static PaymentMethod from(Integer installmentsQuantity, BigDecimal installmentsRate,
                                   BigDecimal installmentAmount, Boolean acceptsCreditCard,
                                   Boolean acceptsDebitCard, Boolean acceptsMercadoPago) {
        validatePaymentMethodParameters(installmentsQuantity, installmentAmount);
        
        return new PaymentMethod(
            installmentsQuantity,
            installmentsRate != null ? installmentsRate : BigDecimal.ZERO,
            installmentAmount,
            acceptsCreditCard != null ? acceptsCreditCard : false,
            acceptsDebitCard != null ? acceptsDebitCard : false,
            acceptsMercadoPago != null ? acceptsMercadoPago : false
        );
    }
    
    private static void validatePaymentMethodParameters(Integer installmentsQuantity, BigDecimal installmentAmount) {
        if (installmentsQuantity != null && installmentsQuantity < 1) {
            throw new IllegalArgumentException("La cantidad de cuotas debe ser mayor a 0");
        }
        
        if (installmentAmount != null && installmentAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de cuota no puede ser negativo");
        }
    }
    
    public Integer getInstallmentsQuantity() {
        return installmentsQuantity;
    }
    
    public BigDecimal getInstallmentsRate() {
        return installmentsRate;
    }
    
    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }
    
    public Boolean getAcceptsCreditCard() {
        return acceptsCreditCard;
    }
    
    public Boolean getAcceptsDebitCard() {
        return acceptsDebitCard;
    }
    
    public Boolean getAcceptsMercadoPago() {
        return acceptsMercadoPago;
    }
    
    public boolean hasInstallments() {
        return installmentsQuantity != null && installmentsQuantity > 1;
    }
    
    public boolean isFreeInstallments() {
        return hasInstallments() && 
               installmentsRate != null && 
               installmentsRate.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(installmentsQuantity, that.installmentsQuantity) &&
               Objects.equals(installmentsRate, that.installmentsRate) &&
               Objects.equals(installmentAmount, that.installmentAmount) &&
               Objects.equals(acceptsCreditCard, that.acceptsCreditCard) &&
               Objects.equals(acceptsDebitCard, that.acceptsDebitCard) &&
               Objects.equals(acceptsMercadoPago, that.acceptsMercadoPago);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(installmentsQuantity, installmentsRate, installmentAmount,
                           acceptsCreditCard, acceptsDebitCard, acceptsMercadoPago);
    }
    
    @Override
    public String toString() {
        return String.format("PaymentMethod{installments=%d, rate=%s, amount=%s}", 
                installmentsQuantity, installmentsRate, installmentAmount);
    }
}