package com.pix.service;

import com.pix.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    // === 기본 CRUD 메서드 ===
    Page<ProductDto.Response> getAllProducts(Pageable pageable);
    ProductDto.DetailResponse getProductById(Long id);
    Page<ProductDto.Response> getProductsByCategory(Long categoryId, Pageable pageable);

    // === 특별 카테고리 조회 메서드 (프론트엔드에서 사용) ===
    Page<ProductDto.Response> getNewProducts(Pageable pageable);
    Page<ProductDto.Response> getBestProducts(Pageable pageable);
    Page<ProductDto.Response> getEventProducts(Pageable pageable);

    // === 검색 및 필터링 ===
    Page<ProductDto.Response> searchProducts(ProductDto.SearchRequest searchRequest);
    Page<ProductDto.Response> searchByName(String name, Pageable pageable);

    // === 유틸리티 메서드 ===
    List<Long> getAllCategoryIds();
    List<ProductDto.Response> getLowStockProducts();
    
    // 🔥 추가된 메서드들
    long getTotalProductCount(); // 총 상품 수 조회

    // === 재고 관리 ===
    boolean checkStock(Long productId, int quantity);
    void decreaseStock(Long productId, int quantity);
    void increaseStock(Long productId, int quantity);

    // === 관리자 기능 ===
    ProductDto.DetailResponse createProduct(ProductDto.CreateRequest request);
    ProductDto.DetailResponse updateProduct(Long id, ProductDto.UpdateRequest request);
    void deleteProduct(Long id);
}