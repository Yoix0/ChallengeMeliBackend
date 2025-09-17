package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.PaymentMethod;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_methods")
public class PaymentMethodEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ItemEntity item;
    
    @Column(name = "installments_quantity")
    private Integer installmentsQuantity;
    
    @Column(name = "installments_rate", precision = 5, scale = 2)
    private BigDecimal installmentsRate;
    
    @Column(name = "installment_amount", precision = 15, scale = 2)
    private BigDecimal installmentAmount;
    
    @Column(name = "accepts_credit_card")
    private Boolean acceptsCreditCard;
    
    @Column(name = "accepts_debit_card")
    private Boolean acceptsDebitCard;
    
    @Column(name = "accepts_mercadopago")
    private Boolean acceptsMercadoPago;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public PaymentMethodEntity() {}
    
    public PaymentMethodEntity(String itemId, Integer installmentsQuantity, BigDecimal installmentsRate,
                              BigDecimal installmentAmount, Boolean acceptsCreditCard,
                              Boolean acceptsDebitCard, Boolean acceptsMercadoPago) {
        this.itemId = itemId;
        this.installmentsQuantity = installmentsQuantity;
        this.installmentsRate = installmentsRate;
        this.installmentAmount = installmentAmount;
        this.acceptsCreditCard = acceptsCreditCard;
        this.acceptsDebitCard = acceptsDebitCard;
        this.acceptsMercadoPago = acceptsMercadoPago;
        this.createdAt = LocalDateTime.now();
    }
    
    public static PaymentMethodEntity fromDomain(String itemId, PaymentMethod paymentMethod) {
        return new PaymentMethodEntity(
                itemId,
                paymentMethod.getInstallmentsQuantity(),
                paymentMethod.getInstallmentsRate(),
                paymentMethod.getInstallmentAmount(),
                paymentMethod.getAcceptsCreditCard(),
                paymentMethod.getAcceptsDebitCard(),
                paymentMethod.getAcceptsMercadoPago()
        );
    }
    
    public PaymentMethod toDomain() {
        return PaymentMethod.from(
                this.installmentsQuantity,
                this.installmentsRate,
                this.installmentAmount,
                this.acceptsCreditCard,
                this.acceptsDebitCard,
                this.acceptsMercadoPago
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public Integer getInstallmentsQuantity() { return installmentsQuantity; }
    public void setInstallmentsQuantity(Integer installmentsQuantity) { this.installmentsQuantity = installmentsQuantity; }
    public BigDecimal getInstallmentsRate() { return installmentsRate; }
    public void setInstallmentsRate(BigDecimal installmentsRate) { this.installmentsRate = installmentsRate; }
    public BigDecimal getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }
    public Boolean getAcceptsCreditCard() { return acceptsCreditCard; }
    public void setAcceptsCreditCard(Boolean acceptsCreditCard) { this.acceptsCreditCard = acceptsCreditCard; }
    public Boolean getAcceptsDebitCard() { return acceptsDebitCard; }
    public void setAcceptsDebitCard(Boolean acceptsDebitCard) { this.acceptsDebitCard = acceptsDebitCard; }
    public Boolean getAcceptsMercadoPago() { return acceptsMercadoPago; }
    public void setAcceptsMercadoPago(Boolean acceptsMercadoPago) { this.acceptsMercadoPago = acceptsMercadoPago; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ItemEntity getItem() { return item; }
    public void setItem(ItemEntity item) { this.item = item; }
}