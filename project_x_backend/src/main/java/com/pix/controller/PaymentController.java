package com.pix.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    // 테스트용 토스페이 키 (실제 운영시에는 application.properties에서 관리)
    @Value("${toss.payments.secret-key:test_sk_5OWRapdA8djNalaeqb4bVo1zEqZK}")
    private String tossSecretKey;
    
    /**
     * 결제 승인 API (토스페이먼츠 결제 완료 후 호출)
     * 실제로는 토스페이먼츠 서버와 통신해야 하지만, 
     * 현재는 테스트용으로 임시 승인 처리
     */
    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody Map<String, Object> paymentRequest) {
        try {
            logger.info("결제 승인 요청 수신: {}", paymentRequest);
            
            String paymentKey = (String) paymentRequest.get("paymentKey");
            String orderId = (String) paymentRequest.get("orderId");
            Integer amount = (Integer) paymentRequest.get("amount");
            
            // 기본 유효성 검사
            if (!StringUtils.hasText(paymentKey) || !StringUtils.hasText(orderId) || amount == null || amount <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "필수 파라미터가 누락되었습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // TODO: 실제 토스페이먼츠 API 호출
            // 현재는 테스트용으로 성공 응답 반환
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "결제가 성공적으로 승인되었습니다.");
            
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("paymentKey", paymentKey);
            paymentData.put("orderId", orderId);
            paymentData.put("amount", amount);
            paymentData.put("status", "DONE");
            paymentData.put("approvedAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            paymentData.put("method", "CARD");
            paymentData.put("currency", "KRW");
            
            response.put("data", paymentData);
            
            logger.info("결제 승인 완료: orderId={}, amount={}", orderId, amount);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("결제 승인 처리 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "결제 승인 처리 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 결제 취소 API
     */
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(@RequestBody Map<String, Object> cancelRequest) {
        try {
            logger.info("결제 취소 요청 수신: {}", cancelRequest);
            
            String paymentKey = (String) cancelRequest.get("paymentKey");
            String cancelReason = (String) cancelRequest.get("cancelReason");
            
            if (!StringUtils.hasText(paymentKey)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "paymentKey가 누락되었습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // TODO: 실제 토스페이먼츠 취소 API 호출
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "결제가 성공적으로 취소되었습니다.");
            
            Map<String, Object> cancelData = new HashMap<>();
            cancelData.put("paymentKey", paymentKey);
            cancelData.put("status", "CANCELED");
            cancelData.put("canceledAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            cancelData.put("cancelReason", cancelReason != null ? cancelReason : "고객 요청");
            
            response.put("data", cancelData);
            
            logger.info("결제 취소 완료: paymentKey={}", paymentKey);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("결제 취소 처리 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "결제 취소 처리 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 결제 정보 조회 API
     */
    @GetMapping("/{paymentKey}")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable String paymentKey) {
        try {
            logger.info("결제 정보 조회 요청: paymentKey={}", paymentKey);
            
            if (!StringUtils.hasText(paymentKey)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "paymentKey가 필요합니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // TODO: 실제 토스페이먼츠 조회 API 호출 또는 DB 조회
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("paymentKey", paymentKey);
            paymentData.put("orderId", "order_" + System.currentTimeMillis());
            paymentData.put("amount", 10000);
            paymentData.put("status", "DONE");
            paymentData.put("method", "CARD");
            paymentData.put("currency", "KRW");
            paymentData.put("approvedAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            response.put("data", paymentData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("결제 정보 조회 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "결제 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 결제 URL 생성 API (추가 기능)
     */
    @PostMapping("/create-payment-url")
    public ResponseEntity<Map<String, Object>> createPaymentUrl(@RequestBody Map<String, Object> paymentRequest) {
        try {
            logger.info("결제 URL 생성 요청: {}", paymentRequest);
            
            String orderId = (String) paymentRequest.get("orderId");
            if (orderId == null) {
                orderId = "order_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("successUrl", "http://localhost:5173/MD/payment/success");
            response.put("failUrl", "http://localhost:5173/MD/payment/fail");
            response.put("createdAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("결제 URL 생성 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "결제 URL 생성 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
 
}