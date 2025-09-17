package com.pix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")  // 🟢 소문자 테이블명
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 🟢 IDENTITY 사용
    @Column(name = "id")
    private Long id;

    // 🟢 로그인 사용자용 (nullable)
    @Column(name = "user_id")
    private Long userId;

    // 🟢 비로그인 사용자용 (nullable) 
    @Column(name = "session_id", length = 100)
    private String sessionId;

    // 🟢 Product와의 관계는 ID로만 관리 (단순화)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "selected_option", length = 100)
    private String selectedOption;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 🟢 총 가격 계산 메서드
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    // 🟢 수량 증가
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    // 🟢 수량 변경
    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }
        this.quantity = newQuantity;
    }

    // 🟢 정적 팩토리 메서드 (세션용)
    public static CartItem forSession(String sessionId, Long productId, int quantity, BigDecimal price, String selectedOption) {
        return CartItem.builder()
                .sessionId(sessionId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .selectedOption(selectedOption)
                .build();
    }

    // 🟢 정적 팩토리 메서드 (로그인 사용자용)
    public static CartItem forUser(Long userId, Long productId, int quantity, BigDecimal price, String selectedOption) {
        return CartItem.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .selectedOption(selectedOption)
                .build();
    }
}