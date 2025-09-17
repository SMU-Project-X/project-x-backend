package com.pix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")  // 🟢 소문자 테이블명
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 🟢 IDENTITY 사용
    @Column(name = "id")  // 🟢 간단한 컬럼명
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10)
    private BigDecimal originalPrice;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "stock_quantity", nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    // 🟢 Category는 FK가 아닌 ID로 관리 (단순화)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "is_new")
    @Builder.Default
    private Boolean isNew = false;

    @Column(name = "has_event")
    @Builder.Default
    private Boolean hasEvent = false;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(name = "options", length = 500)
    private String options;

    @Column(name = "image_urls", length = 1000)
    private String imageUrls;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 🟢 비즈니스 메서드들은 새 스키마에 맞게 수정
    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
}