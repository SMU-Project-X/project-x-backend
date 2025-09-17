package com.pix.dto;

import com.pix.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.util.*;
//import javax.validation.constraints.*;

public class ProductDto {

    /**
     * 상품 목록 응답용 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private Integer discountRate;
        private Integer stockQuantity;
        private Long categoryId;
        private Boolean isNew;
        private Boolean hasEvent;
        private BigDecimal averageRating;
        private Integer reviewCount;
        private String options;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // 가격 관련 계산 메서드
        public BigDecimal getDiscountedPrice() {
            if (originalPrice != null && discountRate != null && discountRate > 0) {
                return originalPrice.multiply(BigDecimal.valueOf(100 - discountRate))
                        .divide(BigDecimal.valueOf(100));
            }
            return price;
        }

        public boolean isOnSale() {
            return originalPrice != null && discountRate != null && discountRate > 0;
        }

        public boolean isInStock() {
            return stockQuantity != null && stockQuantity > 0;
        }

        // Entity에서 DTO로 변환
        public static Response from(Product product) {
            return Response.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .originalPrice(product.getOriginalPrice())
                    .discountRate(product.getDiscountRate())
                    .categoryId(product.getCategoryId())
                    .isNew(product.getIsNew() != null ? product.getIsNew() : false)
                    .hasEvent(product.getHasEvent() != null ? product.getHasEvent() : false)
                    .averageRating(product.getAverageRating())
                    .reviewCount(product.getReviewCount() != null ? product.getReviewCount() : 0)
                    .stockQuantity(product.getStockQuantity())
                    .imageUrls(product.getImageUrls() != null && !product.getImageUrls().isEmpty() ? 
                              java.util.Arrays.asList(product.getImageUrls().split(",")) : 
                              java.util.Collections.singletonList("https://via.placeholder.com/300x300/CCCCCC/FFFFFF?text=No+Image"))
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .build();
        }
    }

    /**
     * 상품 상세 응답용 DTO (Response보다 더 많은 정보 포함)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private Integer discountRate;
        private Integer stockQuantity;
        private Long categoryId;
        private Boolean isNew;
        private Boolean hasEvent;
        private BigDecimal averageRating;
        private Integer reviewCount;
        private String options;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // 추가 상세 정보
        private boolean inStock;
        private boolean onSale;
        private BigDecimal finalPrice;

        // Entity에서 DTO로 변환
        public static DetailResponse from(Product product) {
            BigDecimal finalPrice = product.getPrice();
            boolean onSale = false;
            
            // 할인 가격 계산
            if (product.getOriginalPrice() != null && product.getDiscountRate() != null && product.getDiscountRate() > 0) {
                finalPrice = product.getOriginalPrice().multiply(
                    BigDecimal.valueOf(100 - product.getDiscountRate()).divide(BigDecimal.valueOf(100))
                );
                onSale = true;
            }
            
            DetailResponse response = DetailResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .originalPrice(product.getOriginalPrice())
                    .discountRate(product.getDiscountRate())
                    .stockQuantity(product.getStockQuantity())
                    .categoryId(product.getCategoryId())
                    .isNew(product.getIsNew() != null ? product.getIsNew() : false)
                    .hasEvent(product.getHasEvent() != null ? product.getHasEvent() : false)
                    .averageRating(product.getAverageRating())
                    .reviewCount(product.getReviewCount() != null ? product.getReviewCount() : 0)
                    .options(product.getOptions())
                    .imageUrls(product.getImageUrls() != null && !product.getImageUrls().isEmpty() ? 
                              java.util.Arrays.asList(product.getImageUrls().split(",")) : 
                              java.util.Collections.singletonList("https://via.placeholder.com/300x300/CCCCCC/FFFFFF?text=No+Image"))
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .inStock(product.getStockQuantity() != null && product.getStockQuantity() > 0)
                    .onSale(onSale)
                    .finalPrice(finalPrice)
                    .build();
                    
            return response;
        }
    }

    /**
     * 상품 생성 요청용 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "상품명은 필수입니다")
        @Size(max = 200, message = "상품명은 200자 이하여야 합니다")
        private String name;

        @Size(max = 2000, message = "상품 설명은 2000자 이하여야 합니다")
        private String description;

        @NotNull(message = "가격은 필수입니다")
        @DecimalMin(value = "0", message = "가격은 0 이상이어야 합니다")
        private BigDecimal price;

        @DecimalMin(value = "0", message = "원가는 0 이상이어야 합니다")
        private BigDecimal originalPrice;

        @Min(value = 0, message = "할인율은 0% 이상이어야 합니다")
        @Max(value = 100, message = "할인율은 100% 이하여야 합니다")
        private Integer discountRate;

        @NotNull(message = "카테고리는 필수입니다")
        private Long categoryId;

        @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
        @Builder.Default
        private Integer stockQuantity = 0;

        @Builder.Default
        private Boolean isNew = false;

        @Builder.Default
        private Boolean hasEvent = false;

        @DecimalMin(value = "0.0", message = "평점은 0.0 이상이어야 합니다")
        @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
        private BigDecimal averageRating;

        @Min(value = 0, message = "리뷰 수는 0 이상이어야 합니다")
        @Builder.Default
        private Integer reviewCount = 0;

        @Size(max = 500, message = "옵션은 500자 이하여야 합니다")
        private String options;

        @Size(max = 1000, message = "이미지 URL은 1000자 이하여야 합니다")
        private String imageUrls;

        // DTO에서 Entity로 변환
        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .originalPrice(originalPrice)
                    .discountRate(discountRate)
                    .categoryId(categoryId)
                    .stockQuantity(stockQuantity)
                    .isNew(isNew)
                    .hasEvent(hasEvent)
                    .averageRating(averageRating)
                    .reviewCount(reviewCount)
                    .options(options)
                    .imageUrls(imageUrls)
                    .build();
        }
    }

    /**
     * 상품 수정 요청용 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        @Size(max = 200, message = "상품명은 200자 이하여야 합니다")
        private String name;

        @Size(max = 2000, message = "상품 설명은 2000자 이하여야 합니다")
        private String description;

        @DecimalMin(value = "0", message = "가격은 0 이상이어야 합니다")
        private BigDecimal price;

        @DecimalMin(value = "0", message = "원가는 0 이상이어야 합니다")
        private BigDecimal originalPrice;

        @Min(value = 0, message = "할인율은 0% 이상이어야 합니다")
        @Max(value = 100, message = "할인율은 100% 이하여야 합니다")
        private Integer discountRate;

        private Long categoryId;

        @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
        private Integer stockQuantity;

        private Boolean isNew;
        private Boolean hasEvent;

        @DecimalMin(value = "0.0", message = "평점은 0.0 이상이어야 합니다")
        @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
        private BigDecimal averageRating;

        @Min(value = 0, message = "리뷰 수는 0 이상이어야 합니다")
        private Integer reviewCount;

        @Size(max = 500, message = "옵션은 500자 이하여야 합니다")
        private String options;

        @Size(max = 1000, message = "이미지 URL은 1000자 이하여야 합니다")
        private String imageUrls;
    }

    /**
     * 상품 검색 요청용 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchRequest {
        private String keyword;
        private Long categoryId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Boolean isNew;
        private Boolean hasEvent;
        private Boolean inStockOnly;

        @Builder.Default
        private int page = 0;

        @Builder.Default
        private int size = 20;

        @Builder.Default
        private String sortBy = "createdAt";

        @Builder.Default
        private String sortDirection = "desc";

        // 검증 메서드
        public boolean isValidPriceRange() {
            if (minPrice != null && maxPrice != null) {
                return minPrice.compareTo(maxPrice) <= 0;
            }
            return true;
        }
    }

    /**
     * 상품 요약 정보용 DTO (관리자 대시보드 등에서 사용)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String name;
        private BigDecimal price;
        private Integer stockQuantity;
        private Boolean isNew;
        private Boolean hasEvent;
        private LocalDateTime createdAt;

        public static Summary from(Product product) {
            return Summary.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .isNew(product.getIsNew())
                    .hasEvent(product.getHasEvent())
                    .createdAt(product.getCreatedAt())
                    .build();
        }
    }
 
    
}