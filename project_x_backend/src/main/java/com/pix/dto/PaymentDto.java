package com.pix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * WHY? 프론트엔드와 백엔드 간의 결제 데이터 전송을 위한 DTO 클래스들
 * 토스페이먼츠 API 응답 구조와 내부 비즈니스 로직에 맞게 설계
 */

public class PaymentDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossConfirmRequest {
        @NotBlank(message = "paymentKey는 필수입니다")
        private String paymentKey;
        
        @NotBlank(message = "orderId는 필수입니다")
        private String orderId;
        
        @NotNull(message = "amount는 필수입니다")
        @Positive(message = "amount는 0보다 커야 합니다")
        private BigDecimal amount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentResult {
        private boolean success;
        private String message;
        private String errorCode;
        private String errorMessage;
        private PaymentInfo paymentInfo;
        
        public static PaymentResult success(PaymentInfo paymentInfo) {
            return PaymentResult.builder()
                .success(true)
                .message("결제가 성공적으로 완료되었습니다")
                .paymentInfo(paymentInfo)
                .build();
        }
        
        public static PaymentResult failure(String errorMessage) {
            return PaymentResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
        }
        
        public static PaymentResult failure(String errorCode, String errorMessage) {
            return PaymentResult.builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private String paymentKey;
        private String orderId;
        private String orderName;
        private BigDecimal amount;
        private String method;
        private String status;
        private LocalDateTime requestedAt;
        private LocalDateTime approvedAt;
        private String receiptUrl;
        private String checkoutUrl;
        private CustomerInfo customer;
        private List<OrderItemInfo> orderItems;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private String name;
        private String email;
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private String selectedOption;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelRequest {
        @NotBlank(message = "취소 사유는 필수입니다")
        private String reason;
        
        private BigDecimal cancelAmount; // 부분 취소용
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentHistoryResponse {
        private List<PaymentInfo> payments;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}