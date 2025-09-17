package com.pix.dto;

import com.pix.entity.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartDto {

    // 장바구니 아이템 응답용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemResponse {
        private Long id;
        private Long userId;
        private String sessionId;
        private Long productId;
        private String productName;  // 상품 정보는 별도 조회 필요
        private String productImageUrls;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal totalPrice;
        private String selectedOption;
        private LocalDateTime createdAt;

        // Entity -> DTO 변환
        public static ItemResponse from(CartItem cartItem) {
            return ItemResponse.builder()
                    .id(cartItem.getId())
                    .userId(cartItem.getUserId())
                    .sessionId(cartItem.getSessionId())
                    .productId(cartItem.getProductId())
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .selectedOption(cartItem.getSelectedOption())
                    .createdAt(cartItem.getCreatedAt())
                    .build();
        }

        // 상품 정보 포함한 생성자
        public static ItemResponse fromWithProduct(CartItem cartItem, String productName, String productImageUrls) {
            ItemResponse response = from(cartItem);
            response.setProductName(productName);
            response.setProductImageUrls(productImageUrls);
            return response;
        }
    }

    // 장바구니 전체 응답용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private List<ItemResponse> items;
        private Integer totalCount;
        private BigDecimal subtotal;
        private BigDecimal shippingFee;
        private BigDecimal totalAmount;

        public static Response of(List<ItemResponse> items, BigDecimal shippingFee) {
            BigDecimal subtotal = items.stream()
                    .map(ItemResponse::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Integer totalCount = items.stream()
                    .mapToInt(ItemResponse::getQuantity)
                    .sum();

            BigDecimal totalAmount = subtotal.add(shippingFee);

            return Response.builder()
                    .items(items)
                    .totalCount(totalCount)
                    .subtotal(subtotal)
                    .shippingFee(shippingFee)
                    .totalAmount(totalAmount)
                    .build();
        }
    }

    // 장바구니 아이템 추가 요청용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddRequest {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private Integer quantity;

        @Size(max = 100, message = "옵션은 100자 이하여야 합니다.")
        private String selectedOption;
    }

    // 장바구니 아이템 수량 변경 요청용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateQuantityRequest {
        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private Integer quantity;
    }

    // 장바구니 아이템 옵션 수정 요청용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateOptionsRequest {
        @Size(max = 100, message = "옵션은 100자 이하여야 합니다.")
        private String selectedOption;
    }

    // 결제를 위한 장바구니 요약 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CheckoutSummary {
        private List<ItemResponse> items;
        private BigDecimal subtotal;
        private BigDecimal discount;
        private BigDecimal shippingFee;
        private BigDecimal totalAmount;
        private String sessionId;

        public static CheckoutSummary of(List<ItemResponse> items, String sessionId, 
                                       BigDecimal discount, BigDecimal shippingFee) {
            BigDecimal subtotal = items.stream()
                    .map(ItemResponse::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalAmount = subtotal.subtract(discount).add(shippingFee);

            return CheckoutSummary.builder()
                    .items(items)
                    .subtotal(subtotal)
                    .discount(discount)
                    .shippingFee(shippingFee)
                    .totalAmount(totalAmount)
                    .sessionId(sessionId)
                    .build();
        }
    }
}