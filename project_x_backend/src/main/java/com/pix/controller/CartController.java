package com.pix.controller;

import com.pix.dto.CartDto;
import com.pix.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
// @CrossOrigin 제거 - WebConfig에서 전역으로 처리
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 전체 조회 (세션 기반)
     */
    @GetMapping
    public ResponseEntity<CartDto.Response> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        log.info("장바구니 조회 요청 - 세션 ID: {}", sessionId);
        
        CartDto.Response cartResponse = cartService.getCartItems(sessionId);
        return ResponseEntity.ok(cartResponse);
    }

    /**
     * 장바구니 전체 조회 (사용자 기반)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto.Response> getCartItemsByUserId(@PathVariable Long userId) {
        log.info("장바구니 조회 요청 - 사용자 ID: {}", userId);
        
        CartDto.Response cartResponse = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartResponse);
    }

    /**
     * 장바구니에 상품 추가 (세션 기반)
     */
    @PostMapping("/add")
    public ResponseEntity<CartDto.ItemResponse> addToCart(
            HttpSession session,
            @Valid @RequestBody CartDto.AddRequest request) {
        
        String sessionId = session.getId();
        log.info("장바구니 추가 요청 - 세션 ID: {}, 상품 ID: {}, 수량: {}", 
                sessionId, request.getProductId(), request.getQuantity());
        
        try {
            CartDto.ItemResponse item = cartService.addToCart(sessionId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 추가 실패 - 세션 ID: {}, 원인: {}", sessionId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 장바구니에 상품 추가 (사용자 기반)
     */
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<CartDto.ItemResponse> addToCartByUserId(
            @PathVariable Long userId,
            @Valid @RequestBody CartDto.AddRequest request) {
        
        log.info("장바구니 추가 요청 - 사용자 ID: {}, 상품 ID: {}, 수량: {}", 
                userId, request.getProductId(), request.getQuantity());
        
        try {
            CartDto.ItemResponse item = cartService.addToCartByUserId(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 추가 실패 - 사용자 ID: {}, 원인: {}", userId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 장바구니 아이템 수량 변경
     */
    @PutMapping("/{cartItemId}/quantity")
    public ResponseEntity<CartDto.ItemResponse> updateCartItemQuantity(
            HttpSession session,
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartDto.UpdateQuantityRequest request) {
        
        String sessionId = session.getId();
        log.info("장바구니 수량 변경 요청 - 세션 ID: {}, 아이템 ID: {}, 수량: {}", 
                sessionId, cartItemId, request.getQuantity());
        
        try {
            CartDto.ItemResponse item = cartService.updateCartItemQuantity(sessionId, cartItemId, request);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 수량 변경 실패 - 세션 ID: {}, 아이템 ID: {}, 원인: {}", 
                    sessionId, cartItemId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 장바구니 아이템 옵션 변경
     */
    @PutMapping("/{cartItemId}/options")
    public ResponseEntity<CartDto.ItemResponse> updateCartItemOptions(
            HttpSession session,
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartDto.UpdateOptionsRequest request) {
        
        String sessionId = session.getId();
        log.info("장바구니 옵션 변경 요청 - 세션 ID: {}, 아이템 ID: {}", sessionId, cartItemId);
        
        try {
            CartDto.ItemResponse item = cartService.updateCartItemOptions(sessionId, cartItemId, request);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 옵션 변경 실패 - 세션 ID: {}, 아이템 ID: {}, 원인: {}", 
                    sessionId, cartItemId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 장바구니 아이템 삭제
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(HttpSession session, @PathVariable Long cartItemId) {
        String sessionId = session.getId();
        log.info("장바구니 아이템 삭제 요청 - 세션 ID: {}, 아이템 ID: {}", sessionId, cartItemId);
        
        try {
            cartService.removeFromCart(sessionId, cartItemId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("장바구니 아이템 삭제 실패 - 세션 ID: {}, 아이템 ID: {}, 원인: {}", 
                    sessionId, cartItemId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 장바구니 비우기 (세션 기반)
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpSession session) {
        String sessionId = session.getId();
        log.info("장바구니 비우기 요청 - 세션 ID: {}", sessionId);
        
        cartService.clearCart(sessionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 장바구니 비우기 (사용자 기반)
     */
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCartByUserId(@PathVariable Long userId) {
        log.info("장바구니 비우기 요청 - 사용자 ID: {}", userId);
        
        cartService.clearCartByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 장바구니 아이템 개수 조회 (세션 기반)
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCartItemCount(HttpSession session) {
        String sessionId = session.getId();
        log.debug("장바구니 아이템 개수 조회 - 세션 ID: {}", sessionId);
        
        Long count = cartService.getCartItemCount(sessionId);
        return ResponseEntity.ok(count);
    }

    /**
     * 장바구니 아이템 개수 조회 (사용자 기반)
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getCartItemCountByUserId(@PathVariable Long userId) {
        log.debug("장바구니 아이템 개수 조회 - 사용자 ID: {}", userId);
        
        Long count = cartService.getCartItemCountByUserId(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * 장바구니 총 수량 조회
     */
    @GetMapping("/quantity")
    public ResponseEntity<Long> getCartTotalQuantity(HttpSession session) {
        String sessionId = session.getId();
        log.debug("장바구니 총 수량 조회 - 세션 ID: {}", sessionId);
        
        Long totalQuantity = cartService.getCartTotalQuantity(sessionId);
        return ResponseEntity.ok(totalQuantity);
    }

    /**
     * 장바구니 총 금액 계산
     */
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> calculateTotalPrice(HttpSession session) {
        String sessionId = session.getId();
        log.debug("장바구니 총 금액 계산 - 세션 ID: {}", sessionId);
        
        BigDecimal totalPrice = cartService.calculateTotalPrice(sessionId);
        return ResponseEntity.ok(totalPrice);
    }

    /**
     * 결제를 위한 장바구니 요약 정보
     */
    @GetMapping("/checkout-summary")
    public ResponseEntity<CartDto.CheckoutSummary> getCheckoutSummary(
            HttpSession session,
            @RequestParam(value = "discount", defaultValue = "0") BigDecimal discount) {
        
        String sessionId = session.getId();
        log.info("결제 요약 정보 조회 - 세션 ID: {}, 할인: {}", sessionId, discount);
        
        CartDto.CheckoutSummary summary = cartService.getCheckoutSummary(sessionId, discount);
        return ResponseEntity.ok(summary);
    }

    /**
     * 특정 상품이 장바구니에 있는지 확인
     */
    @GetMapping("/contains/{productId}")
    public ResponseEntity<Boolean> isProductInCart(HttpSession session, @PathVariable Long productId) {
        String sessionId = session.getId();
        log.debug("장바구니 상품 존재 확인 - 세션 ID: {}, 상품 ID: {}", sessionId, productId);
        
        boolean exists = cartService.isProductInCart(sessionId, productId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 배송비 계산
     */
    @GetMapping("/shipping-fee")
    public ResponseEntity<BigDecimal> calculateShippingFee(@RequestParam(value = "subtotal") BigDecimal subtotal) {
        log.debug("배송비 계산 - 소계: {}", subtotal);
        
        BigDecimal shippingFee = cartService.calculateShippingFee(subtotal);
        return ResponseEntity.ok(shippingFee);
    }

    /**
     * 세션 장바구니를 사용자 계정으로 병합 (로그인시)
     */
    @PostMapping("/merge")
    public ResponseEntity<Void> mergeSessionCartToUser(
            HttpSession session,
            @RequestParam(value = "userId") Long userId) {
        
        String sessionId = session.getId();
        log.info("세션 장바구니 병합 요청 - 세션 ID: {}, 사용자 ID: {}", sessionId, userId);
        
        try {
            cartService.mergeSessionCartToUser(sessionId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("장바구니 병합 실패 - 세션 ID: {}, 사용자 ID: {}, 원인: {}", 
                    sessionId, userId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("서버 오류 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
    }
}