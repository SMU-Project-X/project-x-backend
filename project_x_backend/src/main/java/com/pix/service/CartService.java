package com.pix.service;

import com.pix.dto.CartDto;

import java.math.BigDecimal;

public interface CartService {

    /**
     * 장바구니 전체 조회 (세션 기반)
     */
    CartDto.Response getCartItems(String sessionId);

    /**
     * 장바구니 전체 조회 (사용자 기반)
     */
    CartDto.Response getCartItemsByUserId(Long userId);

    /**
     * 장바구니에 상품 추가 (세션 기반)
     */
    CartDto.ItemResponse addToCart(String sessionId, CartDto.AddRequest request);

    /**
     * 장바구니에 상품 추가 (사용자 기반)
     */
    CartDto.ItemResponse addToCartByUserId(Long userId, CartDto.AddRequest request);

    /**
     * 장바구니 아이템 수량 변경
     */
    CartDto.ItemResponse updateCartItemQuantity(String sessionId, Long cartItemId, 
                                               CartDto.UpdateQuantityRequest request);

    /**
     * 장바구니 아이템 옵션 변경
     */
    CartDto.ItemResponse updateCartItemOptions(String sessionId, Long cartItemId, 
                                              CartDto.UpdateOptionsRequest request);

    /**
     * 장바구니 아이템 삭제
     */
    void removeFromCart(String sessionId, Long cartItemId);

    /**
     * 장바구니 비우기 (세션 기반)
     */
    void clearCart(String sessionId);

    /**
     * 장바구니 비우기 (사용자 기반)
     */
    void clearCartByUserId(Long userId);

    /**
     * 장바구니 아이템 개수 조회 (세션 기반)
     */
    Long getCartItemCount(String sessionId);

    /**
     * 장바구니 아이템 개수 조회 (사용자 기반)
     */
    Long getCartItemCountByUserId(Long userId);

    /**
     * 장바구니 총 수량 조회
     */
    Long getCartTotalQuantity(String sessionId);

    /**
     * 장바구니 총 금액 계산
     */
    BigDecimal calculateTotalPrice(String sessionId);

    /**
     * 결제를 위한 장바구니 요약 정보
     */
    CartDto.CheckoutSummary getCheckoutSummary(String sessionId, BigDecimal discount);

    /**
     * 특정 상품이 장바구니에 있는지 확인
     */
    boolean isProductInCart(String sessionId, Long productId);

    /**
     * 배송비 계산
     */
    BigDecimal calculateShippingFee(BigDecimal subtotal);

    /**
     * 세션 장바구니를 사용자 계정으로 병합 (로그인시)
     */
    void mergeSessionCartToUser(String sessionId, Long userId);
}