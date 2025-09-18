package com.pix.service;

import com.pix.dto.CartDto;
import com.pix.entity.CartItem;
import com.pix.entity.Product;
import com.pix.repository.CartRepository;
import com.pix.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("50000");
    private static final BigDecimal SHIPPING_FEE = new BigDecimal("3000");

    @Override
    public CartDto.Response getCartItems(String sessionId) {
        log.debug("장바구니 조회 - 세션 ID: {}", sessionId);
        
        List<CartItem> cartItems = cartRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        List<CartDto.ItemResponse> itemResponses = convertToItemResponses(cartItems);
        
        BigDecimal subtotal = calculateSubtotal(itemResponses);
        BigDecimal shippingFee = calculateShippingFee(subtotal);
        
        return CartDto.Response.of(itemResponses, shippingFee);
    }

    @Override
    public CartDto.Response getCartItemsByUserId(Long userId) {
        log.debug("장바구니 조회 - 사용자 ID: {}", userId);
        
        List<CartItem> cartItems = cartRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<CartDto.ItemResponse> itemResponses = convertToItemResponses(cartItems);
        
        BigDecimal subtotal = calculateSubtotal(itemResponses);
        BigDecimal shippingFee = calculateShippingFee(subtotal);
        
        return CartDto.Response.of(itemResponses, shippingFee);
    }

    @Override
    @Transactional
    public CartDto.ItemResponse addToCart(String sessionId, CartDto.AddRequest request) {
        log.debug("장바구니에 상품 추가 - 세션 ID: {}, 상품 ID: {}", sessionId, request.getProductId());
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + request.getProductId()));
        
        // 재고 확인
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity());
        }
        
        // 기존 장바구니 아이템 확인 (같은 상품, 같은 옵션)
        Optional<CartItem> existingItem = cartRepository.findBySessionIdAndProductIdAndSelectedOption(
                sessionId, request.getProductId(), request.getSelectedOption());
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            // 기존 아이템이 있으면 수량 증가
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            // 총 수량이 재고를 초과하는지 확인
            if (newQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity() + 
                                                 ", 장바구니 수량: " + cartItem.getQuantity());
            }
            
            cartItem.increaseQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice()); // 현재 가격으로 업데이트
        } else {
            // 새 아이템 추가
            cartItem = CartItem.forSession(sessionId, request.getProductId(), 
                    request.getQuantity(), product.getPrice(), request.getSelectedOption());
        }
        
        CartItem savedItem = cartRepository.save(cartItem);
        
        log.info("장바구니 추가 완료 - 상품 ID: {}, 수량: {}", request.getProductId(), request.getQuantity());
        
        // 상품 정보와 함께 응답 생성
        return CartDto.ItemResponse.fromWithProduct(savedItem, product.getName(), product.getImageUrls());
    }

    @Override
    @Transactional
    public CartDto.ItemResponse addToCartByUserId(Long userId, CartDto.AddRequest request) {
        log.debug("장바구니에 상품 추가 - 사용자 ID: {}, 상품 ID: {}", userId, request.getProductId());
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + request.getProductId()));
        
        // 재고 확인
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity());
        }
        
        // 기존 장바구니 아이템 확인
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductIdAndSelectedOption(
                userId, request.getProductId(), request.getSelectedOption());
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            if (newQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity() + 
                                                 ", 장바구니 수량: " + cartItem.getQuantity());
            }
            
            cartItem.increaseQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
        } else {
            cartItem = CartItem.forUser(userId, request.getProductId(), 
                    request.getQuantity(), product.getPrice(), request.getSelectedOption());
        }
        
        CartItem savedItem = cartRepository.save(cartItem);
        
        log.info("장바구니 추가 완료 - 사용자 ID: {}, 상품 ID: {}, 수량: {}", userId, request.getProductId(), request.getQuantity());
        
        return CartDto.ItemResponse.fromWithProduct(savedItem, product.getName(), product.getImageUrls());
    }

    @Override
    @Transactional
    public CartDto.ItemResponse updateCartItemQuantity(String sessionId, Long cartItemId, 
                                                      CartDto.UpdateQuantityRequest request) {
        log.debug("장바구니 수량 변경 - 세션 ID: {}, 아이템 ID: {}, 수량: {}", sessionId, cartItemId, request.getQuantity());
        
        CartItem cartItem = findCartItemBySessionAndId(sessionId, cartItemId);
        
        // 상품 정보 조회 및 재고 확인
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity());
        }
        
        cartItem.updateQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice()); // 현재 가격으로 업데이트
        
        CartItem updatedItem = cartRepository.save(cartItem);
        
        log.info("장바구니 수량 변경 완료 - 아이템 ID: {}, 새 수량: {}", cartItemId, request.getQuantity());
        return CartDto.ItemResponse.fromWithProduct(updatedItem, product.getName(), product.getImageUrls());
    }

    @Override
    @Transactional
    public CartDto.ItemResponse updateCartItemOptions(String sessionId, Long cartItemId, 
                                                     CartDto.UpdateOptionsRequest request) {
        log.debug("장바구니 옵션 변경 - 세션 ID: {}, 아이템 ID: {}", sessionId, cartItemId);
        
        CartItem cartItem = findCartItemBySessionAndId(sessionId, cartItemId);
        cartItem.setSelectedOption(request.getSelectedOption());
        
        CartItem updatedItem = cartRepository.save(cartItem);
        
        // 상품 정보 조회
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        
        log.info("장바구니 옵션 변경 완료 - 아이템 ID: {}", cartItemId);
        return CartDto.ItemResponse.fromWithProduct(updatedItem, product.getName(), product.getImageUrls());
    }

    @Override
    @Transactional
    public void removeFromCart(String sessionId, Long cartItemId) {
        log.debug("장바구니 아이템 삭제 - 세션 ID: {}, 아이템 ID: {}", sessionId, cartItemId);
        
        CartItem cartItem = findCartItemBySessionAndId(sessionId, cartItemId);
        cartRepository.delete(cartItem);
        
        log.info("장바구니 아이템 삭제 완료 - 아이템 ID: {}", cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(String sessionId) {
        log.debug("장바구니 비우기 - 세션 ID: {}", sessionId);
        
        cartRepository.deleteBySessionId(sessionId);
        
        log.info("장바구니 비우기 완료 - 세션 ID: {}", sessionId);
    }

    @Override
    @Transactional
    public void clearCartByUserId(Long userId) {
        log.debug("장바구니 비우기 - 사용자 ID: {}", userId);
        
        cartRepository.deleteByUserId(userId);
        
        log.info("장바구니 비우기 완료 - 사용자 ID: {}", userId);
    }

    @Override
    public Long getCartItemCount(String sessionId) {
        log.debug("장바구니 아이템 개수 조회 - 세션 ID: {}", sessionId);
        
        return cartRepository.countBySessionId(sessionId);
    }

    @Override
    public Long getCartItemCountByUserId(Long userId) {
        log.debug("장바구니 아이템 개수 조회 - 사용자 ID: {}", userId);
        
        return cartRepository.countByUserId(userId);
    }

    @Override
    public Long getCartTotalQuantity(String sessionId) {
        log.debug("장바구니 총 수량 조회 - 세션 ID: {}", sessionId);
        
        return cartRepository.sumQuantityBySessionId(sessionId);
    }

    @Override
    public BigDecimal calculateTotalPrice(String sessionId) {
        log.debug("장바구니 총 금액 계산 - 세션 ID: {}", sessionId);
        
        return cartRepository.calculateTotalPriceBySessionId(sessionId);
    }

    @Override
    public CartDto.CheckoutSummary getCheckoutSummary(String sessionId, BigDecimal discount) {
        log.debug("결제 요약 정보 조회 - 세션 ID: {}", sessionId);
        
        List<CartItem> cartItems = cartRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        List<CartDto.ItemResponse> itemResponses = convertToItemResponses(cartItems);
        
        BigDecimal subtotal = calculateSubtotal(itemResponses);
        BigDecimal shippingFee = calculateShippingFee(subtotal.subtract(discount));
        
        return CartDto.CheckoutSummary.of(itemResponses, sessionId, discount, shippingFee);
    }

    @Override
    public boolean isProductInCart(String sessionId, Long productId) {
        log.debug("장바구니에 상품 존재 확인 - 세션 ID: {}, 상품 ID: {}", sessionId, productId);
        
        return cartRepository.findBySessionIdAndProductId(sessionId, productId).isPresent();
    }

    @Override
    public BigDecimal calculateShippingFee(BigDecimal subtotal) {
        if (subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return SHIPPING_FEE;
    }

    @Override
    @Transactional
    public void mergeSessionCartToUser(String sessionId, Long userId) {
        log.debug("세션 장바구니를 사용자 계정으로 병합 - 세션 ID: {}, 사용자 ID: {}", sessionId, userId);
        
        List<CartItem> sessionItems = cartRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
        
        for (CartItem sessionItem : sessionItems) {
            // 사용자 장바구니에서 같은 상품 + 옵션이 있는지 확인
            Optional<CartItem> existingUserItem = cartRepository.findByUserIdAndProductIdAndSelectedOption(
                    userId, sessionItem.getProductId(), sessionItem.getSelectedOption());
            
            if (existingUserItem.isPresent()) {
                // 기존 아이템이 있으면 수량 합산
                CartItem userItem = existingUserItem.get();
                userItem.increaseQuantity(sessionItem.getQuantity());
                cartRepository.save(userItem);
                
                // 세션 아이템 삭제
                cartRepository.delete(sessionItem);
            } else {
                // 기존 아이템이 없으면 사용자 ID로 변경
                sessionItem.setUserId(userId);
                sessionItem.setSessionId(null);
                cartRepository.save(sessionItem);
            }
        }
        
        log.info("세션 장바구니 병합 완료 - 세션 ID: {}, 사용자 ID: {}", sessionId, userId);
    }

    /**
     * 세션과 아이템 ID로 장바구니 아이템 조회 (권한 확인 포함)
     */
    private CartItem findCartItemBySessionAndId(String sessionId, Long cartItemId) {
        CartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 아이템을 찾을 수 없습니다. ID: " + cartItemId));
        
        if (!sessionId.equals(cartItem.getSessionId())) {
            throw new IllegalArgumentException("해당 장바구니 아이템에 접근할 권한이 없습니다.");
        }
        
        return cartItem;
    }

    /**
     * CartItem 리스트를 ItemResponse 리스트로 변환 (상품 정보 포함)
     */
    private List<CartDto.ItemResponse> convertToItemResponses(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            return List.of();
        }
        
        // 상품 ID 목록 추출
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();
        
        // 상품 정보 한 번에 조회
        Map<Long, Product> productMap = productRepository.findByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        // CartItem과 Product 정보를 결합하여 ItemResponse 생성
        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productMap.get(cartItem.getProductId());
                    if (product != null) {
                        return CartDto.ItemResponse.fromWithProduct(cartItem, product.getName(), product.getImageUrls());
                    } else {
                        // 상품이 삭제된 경우 기본 정보로 생성
                        return CartDto.ItemResponse.from(cartItem);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 소계 계산
     */
    private BigDecimal calculateSubtotal(List<CartDto.ItemResponse> items) {
        return items.stream()
                .map(CartDto.ItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}