package com.pix.controller;

import com.pix.dto.ProductDto;
import com.pix.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ProductController {

    private final ProductService productService;

    // === 기본 CRUD API ===
    
    @GetMapping
    public ResponseEntity<Page<ProductDto.Response>> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("전체 상품 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<ProductDto.Response> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.DetailResponse> getProduct(@PathVariable("id") Long id) {
        log.info("상품 상세 조회 - ID: {}", id);
        
        ProductDto.DetailResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDto.Response>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("카테고리별 상품 조회 - 카테고리 ID: {}", categoryId);
        
        Page<ProductDto.Response> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    // === 특별 카테고리 API ===
    
    @GetMapping("/new")
    public ResponseEntity<Page<ProductDto.Response>> getNewProducts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("신상품 조회");
        
        Page<ProductDto.Response> products = productService.getNewProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best")
    public ResponseEntity<Page<ProductDto.Response>> getBestProducts(
            @PageableDefault(size = 12, sort = "reviewCount", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("베스트상품 조회");
        
        Page<ProductDto.Response> products = productService.getBestProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/events")
    public ResponseEntity<Page<ProductDto.Response>> getEventProducts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("이벤트상품 조회");
        
        Page<ProductDto.Response> products = productService.getEventProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // === 검색 API ===
    
    @PostMapping("/search")
    public ResponseEntity<Page<ProductDto.Response>> searchProducts(@RequestBody ProductDto.SearchRequest searchRequest) {
        log.info("상품 검색 - 키워드: {}, 카테고리: {}", searchRequest.getKeyword(), searchRequest.getCategoryId());
        
        Page<ProductDto.Response> products = productService.searchProducts(searchRequest);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto.Response>> searchByName(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.info("상품명 검색 - 검색어: {}", keyword);
        
        Page<ProductDto.Response> products = productService.searchByName(keyword, pageable);
        return ResponseEntity.ok(products);
    }

    // ⭐⭐ 재고 관리 API - 핵심 부분! ⭐⭐

    /**
     * 상품 재고 수량 조회 (상세페이지에서 사용)
     */
    @GetMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> getStock(@PathVariable Long id) {
        log.info("재고 조회 - 상품 ID: {}", id);
        
        try {
            ProductDto.DetailResponse product = productService.getProductById(id);
            
            Map<String, Object> stockInfo = new HashMap<>();
            stockInfo.put("productId", id);
            stockInfo.put("stockQuantity", product.getStockQuantity());
            stockInfo.put("isInStock", product.getStockQuantity() > 0);
            stockInfo.put("stockStatus", product.getStockQuantity() > 10 ? "충분" : 
                                       product.getStockQuantity() > 0 ? "부족" : "품절");
            
            return ResponseEntity.ok(stockInfo);
            
        } catch (Exception e) {
            log.error("재고 조회 실패 - 상품 ID: {}, 오류: {}", id, e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "재고 정보를 조회할 수 없습니다.");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 재고 확인 (주문 전 체크)
     */
    @PostMapping("/{id}/stock/check")
    public ResponseEntity<Map<String, Object>> checkStock(
            @PathVariable Long id, 
            @RequestParam int quantity) {
        
        log.info("재고 확인 - 상품 ID: {}, 요청 수량: {}", id, quantity);
        
        try {
            boolean available = productService.checkStock(id, quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("available", available);
            response.put("productId", id);
            response.put("requestQuantity", quantity);
            
            if (available) {
                response.put("message", "주문 가능합니다.");
            } else {
                ProductDto.DetailResponse product = productService.getProductById(id);
                response.put("message", "재고가 부족합니다. (현재 재고: " + product.getStockQuantity() + "개)");
                response.put("currentStock", product.getStockQuantity());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("재고 확인 실패 - 상품 ID: {}, 오류: {}", id, e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("available", false);
            errorResponse.put("message", "재고 확인 중 오류가 발생했습니다.");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 🎯 결제 성공시 재고 차감 API (핵심!)
     */
    @PostMapping("/stock/decrease")
    public ResponseEntity<Map<String, Object>> decreaseStockForOrder(@RequestBody Map<String, Object> orderData) {
        log.info("결제 성공 - 재고 차감 요청: {}", orderData);
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");
            
            Map<String, Object> response = new HashMap<>();
            
            // 각 상품별로 재고 차감
            for (Map<String, Object> item : items) {
                Long productId = Long.valueOf(item.get("productId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                
                // 재고 차감 실행
                productService.decreaseStock(productId, quantity);
                log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
            }
            
            response.put("success", true);
            response.put("message", "재고 차감이 완료되었습니다.");
            response.put("processedItems", items.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("재고 차감 실패: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "재고 차감에 실패했습니다: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 주문 취소시 재고 복구 API
     */
    @PostMapping("/stock/increase")
    public ResponseEntity<Map<String, Object>> increaseStockForCancel(@RequestBody Map<String, Object> cancelData) {
        log.info("주문 취소 - 재고 복구 요청: {}", cancelData);
        
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) cancelData.get("items");
            
            Map<String, Object> response = new HashMap<>();
            
            // 각 상품별로 재고 복구
            for (Map<String, Object> item : items) {
                Long productId = Long.valueOf(item.get("productId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                
                // 재고 복구 실행
                productService.increaseStock(productId, quantity);
                log.info("재고 복구 완료 - 상품 ID: {}, 복구 수량: {}", productId, quantity);
            }
            
            response.put("success", true);
            response.put("message", "재고 복구가 완료되었습니다.");
            response.put("processedItems", items.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("재고 복구 실패: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "재고 복구에 실패했습니다: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // === 관리자용 API ===
    
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto.Response>> getLowStockProducts() {
        log.info("저재고 상품 조회");
        
        List<ProductDto.Response> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Long>> getAllCategoryIds() {
        log.info("전체 카테고리 ID 목록 조회");
        
        List<Long> categoryIds = productService.getAllCategoryIds();
        return ResponseEntity.ok(categoryIds);
    }
}