package com.pix.repository;

import com.pix.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    // 세션 ID로 장바구니 아이템 조회
    List<CartItem> findBySessionIdOrderByCreatedAtDesc(String sessionId);

    // 사용자 ID로 장바구니 아이템 조회 (로그인 사용자)
    List<CartItem> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 세션 ID와 상품 ID로 장바구니 아이템 조회
    Optional<CartItem> findBySessionIdAndProductId(String sessionId, Long productId);

    // 사용자 ID와 상품 ID로 장바구니 아이템 조회
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // 세션 ID와 상품 ID, 옵션으로 장바구니 아이템 조회
    Optional<CartItem> findBySessionIdAndProductIdAndSelectedOption(String sessionId, Long productId, String selectedOption);

    // 사용자 ID와 상품 ID, 옵션으로 장바구니 아이템 조회
    Optional<CartItem> findByUserIdAndProductIdAndSelectedOption(Long userId, Long productId, String selectedOption);

    // 세션 ID로 장바구니 아이템 개수 조회
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.sessionId = :sessionId")
    Long countBySessionId(@Param("sessionId") String sessionId);

    // 사용자 ID로 장바구니 아이템 개수 조회
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    // 세션 ID로 장바구니 총 수량 조회
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.sessionId = :sessionId")
    Long sumQuantityBySessionId(@Param("sessionId") String sessionId);

    // 사용자 ID로 장바구니 총 수량 조회
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.userId = :userId")
    Long sumQuantityByUserId(@Param("userId") Long userId);

    // 세션 ID로 장바구니 아이템 모두 삭제
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    // 사용자 ID로 장바구니 아이템 모두 삭제
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // 세션 ID와 상품 ID로 장바구니 아이템 삭제
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.sessionId = :sessionId AND c.productId = :productId")
    void deleteBySessionIdAndProductId(@Param("sessionId") String sessionId, @Param("productId") Long productId);

    // 사용자 ID와 상품 ID로 장바구니 아이템 삭제
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // 특정 기간보다 오래된 장바구니 아이템 삭제 (정리 작업용)
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.createdAt < :cutoffDate")
    void deleteOldCartItems(@Param("cutoffDate") LocalDateTime cutoffDate);

    // 세션 ID로 장바구니 총 금액 계산
    @Query("SELECT COALESCE(SUM(c.price * c.quantity), 0) FROM CartItem c WHERE c.sessionId = :sessionId")
    BigDecimal calculateTotalPriceBySessionId(@Param("sessionId") String sessionId);

    // 사용자 ID로 장바구니 총 금액 계산
    @Query("SELECT COALESCE(SUM(c.price * c.quantity), 0) FROM CartItem c WHERE c.userId = :userId")
    BigDecimal calculateTotalPriceByUserId(@Param("userId") Long userId);

    // 특정 상품이 포함된 모든 장바구니 아이템 조회
    List<CartItem> findByProductId(Long productId);

    // 세션 ID와 날짜 범위로 장바구니 아이템 조회
    @Query("SELECT c FROM CartItem c WHERE c.sessionId = :sessionId AND c.createdAt BETWEEN :startDate AND :endDate ORDER BY c.createdAt DESC")
    List<CartItem> findBySessionIdAndDateRange(@Param("sessionId") String sessionId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    // 사용자 ID와 날짜 범위로 장바구니 아이템 조회
    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.createdAt BETWEEN :startDate AND :endDate ORDER BY c.createdAt DESC")
    List<CartItem> findByUserIdAndDateRange(@Param("userId") Long userId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // 세션 기준 장바구니 통계
    @Query("SELECT c.sessionId, COUNT(c), SUM(c.quantity), SUM(c.price * c.quantity) " +
           "FROM CartItem c WHERE c.sessionId IS NOT NULL GROUP BY c.sessionId")
    List<Object[]> getCartStatisticsBySession();

    // 사용자 기준 장바구니 통계
    @Query("SELECT c.userId, COUNT(c), SUM(c.quantity), SUM(c.price * c.quantity) " +
           "FROM CartItem c WHERE c.userId IS NOT NULL GROUP BY c.userId")
    List<Object[]> getCartStatisticsByUser();

    // 세션을 사용자 계정으로 병합 (로그인시)
    @Modifying
    @Query("UPDATE CartItem c SET c.userId = :userId, c.sessionId = null WHERE c.sessionId = :sessionId")
    void mergeSessionToUser(@Param("sessionId") String sessionId, @Param("userId") Long userId);

    // 중복 상품 확인 후 수량 합계 처리용 조회
    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId AND c.selectedOption = :selectedOption")
    Optional<CartItem> findExistingCartItem(@Param("userId") Long userId, 
                                           @Param("productId") Long productId, 
                                           @Param("selectedOption") String selectedOption);
}