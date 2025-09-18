package com.pix.repository;

import com.pix.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // === 기본 조회 메서드 ===
    Page<Product> findByOrderByCreatedAtDesc(Pageable pageable);

    // === 카테고리별 조회 ===
    Page<Product> findByCategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);

    // === 특별 상품 조회 (프론트엔드에서 사용) ===
    Page<Product> findByIsNewTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findByHasEventTrueOrderByCreatedAtDesc(Pageable pageable);

    // === 검색 관련 메서드 ===
    Page<Product> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name, Pageable pageable);

    // 가격 범위로 검색
    Page<Product> findByPriceBetweenOrderByCreatedAtDesc(
            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // 재고가 있는 상품 조회
    Page<Product> findByStockQuantityGreaterThanOrderByCreatedAtDesc(
            Integer stock, Pageable pageable);

    // === 복합 검색 쿼리 ===
    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.stockQuantity > 0")
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    // === 통계 및 분석용 쿼리 ===
    
    // 카테고리 ID 목록 조회
    @Query("SELECT DISTINCT p.categoryId FROM Product p ORDER BY p.categoryId")
    List<Long> findDistinctCategoryIds();

    // 품절 임박 상품 조회 (재고 10개 이하)
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= 10 AND p.stockQuantity > 0 ORDER BY p.stockQuantity ASC")
    List<Product> findLowStockProducts();

    // 리뷰가 많은 상품 조회 (베스트상품용)
    @Query("SELECT p FROM Product p WHERE p.reviewCount > 0 ORDER BY p.reviewCount DESC, p.averageRating DESC")
    Page<Product> findByOrderByReviewCountDescAverageRatingDesc(Pageable pageable);

    // === 관리자용 쿼리 ===
    
    // 특정 기간 내 생성된 상품 조회
    @Query("SELECT p FROM Product p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate ORDER BY p.createdAt DESC")
    List<Product> findProductsCreatedBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 카테고리별 상품 수 조회
    @Query("SELECT p.categoryId, COUNT(p) FROM Product p GROUP BY p.categoryId")
    List<Object[]> findProductCountByCategory();

    // 평균 가격 조회
    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal findAveragePrice();

    // 총 상품 수 조회 (재고가 있는 상품만)
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity > 0")
    long countProductsInStock();

    // === 재고 관리용 쿼리 ===
    
    // 품절 상품 조회
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 ORDER BY p.name")
    List<Product> findOutOfStockProducts();

    // 특정 재고 이하 상품 조회
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold ORDER BY p.stockQuantity ASC, p.name")
    List<Product> findProductsWithStockLessThanEqual(@Param("threshold") Integer threshold);

    // === 성능 최적화용 쿼리 ===
    
    // 상품 존재 여부만 확인 (성능 최적화)
    boolean existsById(Long id);

    // 카테고리에 상품이 있는지 확인
    boolean existsByCategoryId(Long categoryId);

    // 특정 가격대 상품 존재 여부 확인
    boolean existsByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // === 추천 시스템용 쿼리 ===
    
    // 비슷한 가격대 상품 추천
    @Query("SELECT p FROM Product p WHERE p.id != :productId AND " +
           "p.price BETWEEN :minPrice AND :maxPrice AND " +
           "p.stockQuantity > 0 ORDER BY FUNCTION('ABS', p.price - :targetPrice)")
    List<Product> findSimilarPriceProducts(
            @Param("productId") Long productId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("targetPrice") BigDecimal targetPrice,
            Pageable pageable);

    // 같은 카테고리 다른 상품 추천
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.id != :productId AND " +
           "p.stockQuantity > 0 ORDER BY p.averageRating DESC, p.reviewCount DESC")
    List<Product> findSameCategoryProducts(
            @Param("categoryId") Long categoryId,
            @Param("productId") Long productId,
            Pageable pageable);
    
    List<Product> findByIdIn(List<Long> ids);

}