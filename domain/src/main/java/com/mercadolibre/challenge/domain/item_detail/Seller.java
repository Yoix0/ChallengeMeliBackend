package com.mercadolibre.challenge.domain.item_detail;

import com.mercadolibre.challenge.domain.common.exception.ValidateArgument;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Seller {
    
    private final Long id;
    private final String nickname;
    private final String permalink;
    private final LocalDateTime registrationDate;
    private final String countryId;
    private final String reputationLevel;
    private final String powerSellerStatus;
    private final Integer transactionsCompleted;
    private final Integer transactionsCanceled;
    private final BigDecimal ratingPositive;
    private final BigDecimal ratingNegative;
    private final BigDecimal ratingNeutral;
    
    private Seller(Long id, String nickname, String permalink, LocalDateTime registrationDate,
                  String countryId, String reputationLevel, String powerSellerStatus,
                  Integer transactionsCompleted, Integer transactionsCanceled,
                  BigDecimal ratingPositive, BigDecimal ratingNegative, BigDecimal ratingNeutral) {
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
    }
    
    public static Seller from(Long id, String nickname, String permalink, LocalDateTime registrationDate,
                             String countryId, String reputationLevel, String powerSellerStatus,
                             Integer transactionsCompleted, Integer transactionsCanceled,
                             BigDecimal ratingPositive, BigDecimal ratingNegative, BigDecimal ratingNeutral) {
        validateSellerParameters(id, nickname);
        
        return new Seller(
            id,
            nickname.trim(),
            permalink,
            registrationDate,
            countryId,
            reputationLevel,
            powerSellerStatus,
            transactionsCompleted != null ? transactionsCompleted : 0,
            transactionsCanceled != null ? transactionsCanceled : 0,
            ratingPositive != null ? ratingPositive : BigDecimal.ZERO,
            ratingNegative != null ? ratingNegative : BigDecimal.ZERO,
            ratingNeutral != null ? ratingNeutral : BigDecimal.ZERO
        );
    }
    
    private static void validateSellerParameters(Long id, String nickname) {
        ValidateArgument.validateNotNull(id, "id");
        ValidateArgument.validateStringNotNullAndNotEmpty(nickname, "nickname");
        ValidateArgument.validateLength(nickname.length(), 1, 100, "nickname");
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del seller debe ser mayor a 0");
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public String getPermalink() {
        return permalink;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public String getCountryId() {
        return countryId;
    }
    
    public String getReputationLevel() {
        return reputationLevel;
    }
    
    public String getPowerSellerStatus() {
        return powerSellerStatus;
    }
    
    public Integer getTransactionsCompleted() {
        return transactionsCompleted;
    }
    
    public Integer getTransactionsCanceled() {
        return transactionsCanceled;
    }
    
    public BigDecimal getRatingPositive() {
        return ratingPositive;
    }
    
    public BigDecimal getRatingNegative() {
        return ratingNegative;
    }
    
    public BigDecimal getRatingNeutral() {
        return ratingNeutral;
    }
    
    public boolean isPowerSeller() {
        return powerSellerStatus != null && !powerSellerStatus.trim().isEmpty();
    }
    
    public Integer getTotalTransactions() {
        return transactionsCompleted + transactionsCanceled;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(id, seller.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Seller{id=%d, nickname='%s', reputationLevel='%s'}", 
                id, nickname, reputationLevel);
    }
}