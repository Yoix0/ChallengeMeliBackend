package com.mercadolibre.challenge.infrastructure.h2.item_detail.entity;

import com.mercadolibre.challenge.domain.item_detail.Seller;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
public class SellerEntity {
    
    @Id
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nickname;
    
    @Column(length = 500)
    private String permalink;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    @Column(name = "country_id", length = 10)
    private String countryId;
    
    @Column(name = "reputation_level", length = 20)
    private String reputationLevel;
    
    @Column(name = "power_seller_status", length = 20)
    private String powerSellerStatus;
    
    @Column(name = "transactions_completed")
    private Integer transactionsCompleted;
    
    @Column(name = "transactions_canceled")
    private Integer transactionsCanceled;
    
    @Column(name = "rating_positive", precision = 5, scale = 4)
    private BigDecimal ratingPositive;
    
    @Column(name = "rating_negative", precision = 5, scale = 4)
    private BigDecimal ratingNegative;
    
    @Column(name = "rating_neutral", precision = 5, scale = 4)
    private BigDecimal ratingNeutral;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public SellerEntity() {}
    
    public SellerEntity(Long id, String nickname, String permalink, LocalDateTime registrationDate,
                       String countryId, String reputationLevel, String powerSellerStatus,
                       Integer transactionsCompleted, Integer transactionsCanceled,
                       BigDecimal ratingPositive, BigDecimal ratingNegative, BigDecimal ratingNeutral,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.permalink = permalink;
        this.registrationDate = registrationDate;
        this.countryId = countryId;
        this.reputationLevel = reputationLevel;
        this.powerSellerStatus = powerSellerStatus;
        this.transactionsCompleted = transactionsCompleted;
        this.transactionsCanceled = transactionsCanceled;
        this.ratingPositive = ratingPositive;
        this.ratingNegative = ratingNegative;
        this.ratingNeutral = ratingNeutral;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static SellerEntity fromDomain(Seller seller) {
        return new SellerEntity(
                seller.getId(),
                seller.getNickname(),
                seller.getPermalink(),
                seller.getRegistrationDate(),
                seller.getCountryId(),
                seller.getReputationLevel(),
                seller.getPowerSellerStatus(),
                seller.getTransactionsCompleted(),
                seller.getTransactionsCanceled(),
                seller.getRatingPositive(),
                seller.getRatingNegative(),
                seller.getRatingNeutral(),
                null,
                null
        );
    }
    
    public Seller toDomain() {
        return Seller.from(
                this.id,
                this.nickname,
                this.permalink,
                this.registrationDate,
                this.countryId,
                this.reputationLevel,
                this.powerSellerStatus,
                this.transactionsCompleted,
                this.transactionsCanceled,
                this.ratingPositive,
                this.ratingNegative,
                this.ratingNeutral
        );
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPermalink() { return permalink; }
    public void setPermalink(String permalink) { this.permalink = permalink; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public String getCountryId() { return countryId; }
    public void setCountryId(String countryId) { this.countryId = countryId; }
    public String getReputationLevel() { return reputationLevel; }
    public void setReputationLevel(String reputationLevel) { this.reputationLevel = reputationLevel; }
    public String getPowerSellerStatus() { return powerSellerStatus; }
    public void setPowerSellerStatus(String powerSellerStatus) { this.powerSellerStatus = powerSellerStatus; }
    public Integer getTransactionsCompleted() { return transactionsCompleted; }
    public void setTransactionsCompleted(Integer transactionsCompleted) { this.transactionsCompleted = transactionsCompleted; }
    public Integer getTransactionsCanceled() { return transactionsCanceled; }
    public void setTransactionsCanceled(Integer transactionsCanceled) { this.transactionsCanceled = transactionsCanceled; }
    public BigDecimal getRatingPositive() { return ratingPositive; }
    public void setRatingPositive(BigDecimal ratingPositive) { this.ratingPositive = ratingPositive; }
    public BigDecimal getRatingNegative() { return ratingNegative; }
    public void setRatingNegative(BigDecimal ratingNegative) { this.ratingNegative = ratingNegative; }
    public BigDecimal getRatingNeutral() { return ratingNeutral; }
    public void setRatingNeutral(BigDecimal ratingNeutral) { this.ratingNeutral = ratingNeutral; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}