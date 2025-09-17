package com.mercadolibre.challenge.infrastructure.h2.item_detail.repository;

import com.mercadolibre.challenge.infrastructure.h2.item_detail.entity.ItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaItemRepository extends JpaRepository<ItemEntity, String> {

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category
            LEFT JOIN FETCH i.seller
            WHERE i.id = :id
            """)
    Optional<ItemEntity> findByIdWithDetails(@Param("id") String id);

    @Query("""
            SELECT DISTINCT i FROM ItemEntity i
            LEFT JOIN i.category c
            LEFT JOIN i.seller s
            LEFT JOIN i.shippingMethods sm
            WHERE (:query IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:categoryId IS NULL OR c.id = :categoryId)
            AND (:minPrice IS NULL OR i.priceAmount >= :minPrice)
            AND (:maxPrice IS NULL OR i.priceAmount <= :maxPrice)
            AND (:condition IS NULL OR i.conditionType = :condition)
            AND (:freeShipping IS NULL OR :freeShipping = FALSE OR i.freeShipping = TRUE OR EXISTS (SELECT 1 FROM i.shippingMethods sm2 WHERE sm2.freeShipping = TRUE))
            AND (:sellerReputation IS NULL OR s.reputationLevel = :sellerReputation)
            AND i.status = 'active'
            """)
    List<ItemEntity> findItemsBySearchCriteria(
            @Param("query") String query,
            @Param("categoryId") String categoryId,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("condition") String condition,
            @Param("freeShipping") Boolean freeShipping,
            @Param("sellerReputation") String sellerReputation,
            Pageable pageable
    );

    @Query("""
            SELECT COUNT(DISTINCT i) FROM ItemEntity i
            LEFT JOIN i.category c
            LEFT JOIN i.seller s
            LEFT JOIN i.shippingMethods sm
            WHERE (:query IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:categoryId IS NULL OR c.id = :categoryId)
            AND (:minPrice IS NULL OR i.priceAmount >= :minPrice)
            AND (:maxPrice IS NULL OR i.priceAmount <= :maxPrice)
            AND (:condition IS NULL OR i.conditionType = :condition)
            AND (:freeShipping IS NULL OR :freeShipping = FALSE OR i.freeShipping = TRUE OR EXISTS (SELECT 1 FROM i.shippingMethods sm2 WHERE sm2.freeShipping = TRUE))
            AND (:sellerReputation IS NULL OR s.reputationLevel = :sellerReputation)
            AND i.status = 'active'
            """)
    int countItemsBySearchCriteria(
            @Param("query") String query,
            @Param("categoryId") String categoryId,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("condition") String condition,
            @Param("freeShipping") Boolean freeShipping,
            @Param("sellerReputation") String sellerReputation
    );

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category
            LEFT JOIN FETCH i.seller
            WHERE i.status = 'active'
            ORDER BY i.soldQuantity DESC
            """)
    List<ItemEntity> findBestSellers(Pageable pageable);

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category c
            LEFT JOIN FETCH i.seller
            WHERE c.id = :categoryId AND i.status = 'active'
            ORDER BY i.soldQuantity DESC, i.createdDate DESC
            """)
    List<ItemEntity> findTrendingByCategory(@Param("categoryId") String categoryId, Pageable pageable);

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category c1
            LEFT JOIN FETCH i.seller
            WHERE c1.id = (SELECT c2.id FROM ItemEntity i2 LEFT JOIN i2.category c2 WHERE i2.id = :itemId)
            AND i.id != :itemId
            AND i.status = 'active'
            ORDER BY i.soldQuantity DESC
            """)
    List<ItemEntity> findSimilarItemsByCategory(@Param("itemId") String itemId, Pageable pageable);

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category
            LEFT JOIN FETCH i.seller
            WHERE i.category.id = :categoryId AND i.status = 'active'
            ORDER BY i.createdDate DESC
            """)
    List<ItemEntity> findByCategory(@Param("categoryId") String categoryId, Pageable pageable);

    @Query("""
            SELECT i FROM ItemEntity i
            LEFT JOIN FETCH i.category
            LEFT JOIN FETCH i.seller
            WHERE i.seller.id = :sellerId AND i.status = 'active'
            ORDER BY i.createdDate DESC
            """)
    List<ItemEntity> findBySeller(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("""
            SELECT DISTINCT a.attributeValue FROM ItemAttributeEntity a
            LEFT JOIN a.item i
            WHERE a.attributeId = :attributeId
            AND (:categoryId IS NULL OR i.category.id = :categoryId)
            AND i.status = 'active'
            ORDER BY a.attributeValue
            """)
    List<String> findDistinctAttributeValues(@Param("attributeId") String attributeId, @Param("categoryId") String categoryId);
}