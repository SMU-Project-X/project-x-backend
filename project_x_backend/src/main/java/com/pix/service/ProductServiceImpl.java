package com.pix.service;

import com.pix.dto.ProductDto;
import com.pix.entity.Product;
import com.pix.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // === 기본 CRUD 메서드 ===

    @Override
    public Page<ProductDto.Response> getAllProducts(Pageable pageable) {
        log.debug("전체 상품 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Product> products = productRepository.findByOrderByCreatedAtDesc(pageable);
        return products.map(ProductDto.Response::from);
    }

    @Override
    public ProductDto.DetailResponse getProductById(Long id) {
        log.debug("상품 상세 조회 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + id));
        
        return ProductDto.DetailResponse.from(product);
    }

    @Override
    public Page<ProductDto.Response> getProductsByCategory(Long categoryId, Pageable pageable) {
        log.debug("카테고리별 상품 조회 - 카테고리 ID: {}", categoryId);
        
        Page<Product> products = productRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
        return products.map(ProductDto.Response::from);
    }

    // === 특별 카테고리 조회 메서드 ===

    @Override
    public Page<ProductDto.Response> getNewProducts(Pageable pageable) {
        log.debug("신상품 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Product> products = productRepository.findByIsNewTrueOrderByCreatedAtDesc(pageable);
        return products.map(ProductDto.Response::from);
    }

    @Override
    public Page<ProductDto.Response> getBestProducts(Pageable pageable) {
        log.debug("베스트상품 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        // 리뷰 수와 평점을 종합하여 베스트상품으로 간주
        Page<Product> products = productRepository.findByOrderByReviewCountDescAverageRatingDesc(pageable);
        return products.map(ProductDto.Response::from);
    }

    @Override
    public Page<ProductDto.Response> getEventProducts(Pageable pageable) {
        log.debug("이벤트상품 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Product> products = productRepository.findByHasEventTrueOrderByCreatedAtDesc(pageable);
        return products.map(ProductDto.Response::from);
    }

    // === 검색 및 필터링 ===

    @Override
    public Page<ProductDto.Response> searchProducts(ProductDto.SearchRequest searchRequest) {
        log.debug("상품 검색 - 키워드: {}, 카테고리 ID: {}", searchRequest.getKeyword(), searchRequest.getCategoryId());
        
        // 정렬 조건 설정
        Sort sort = createSort(searchRequest.getSortBy(), searchRequest.getSortDirection());
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        
        Page<Product> products = productRepository.searchProducts(
                searchRequest.getKeyword(),
                searchRequest.getCategoryId(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                pageable
        );
        
        log.debug("검색 결과: {}개", products.getContent().size());
        return products.map(ProductDto.Response::from);
    }

    @Override
    public Page<ProductDto.Response> searchByName(String name, Pageable pageable) {
        log.debug("상품명 검색 - 검색어: {}", name);
        
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseOrderByCreatedAtDesc(name, pageable);
        return products.map(ProductDto.Response::from);
    }

    // === 유틸리티 메서드 ===

    @Override
    public List<Long> getAllCategoryIds() {
        log.debug("전체 카테고리 ID 목록 조회");
        
        return productRepository.findDistinctCategoryIds();
    }

    @Override
    public List<ProductDto.Response> getLowStockProducts() {
        log.debug("품절 임박 상품 조회");
        
        List<Product> products = productRepository.findLowStockProducts();
        return products.stream()
                .map(ProductDto.Response::from)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalProductCount() {
        log.debug("총 상품 수 조회");
        
        return productRepository.count();
    }

    // === 재고 관리 ===

    @Override
    public boolean checkStock(Long productId, int quantity) {
        log.debug("재고 확인 - 상품 ID: {}, 요청 수량: {}", productId, quantity);
        
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));
            
            boolean available = product.getStockQuantity() >= quantity;
            log.debug("재고 확인 결과 - 현재 재고: {}, 요청 수량: {}, 가능 여부: {}", 
                     product.getStockQuantity(), quantity, available);
            
            return available;
        } catch (Exception e) {
            log.error("재고 확인 실패 - 상품 ID: {}, 오류: {}", productId, e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        log.debug("재고 차감 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));
        
        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity() + 
                                          ", 요청 수량: " + quantity);
        }
        
        int originalStock = product.getStockQuantity();
        product.setStockQuantity(originalStock - quantity);
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepository.save(product);
        
        log.info("재고 차감 완료 - 상품 ID: {}, 기존 재고: {}, 차감 수량: {}, 현재 재고: {}", 
                 productId, originalStock, quantity, product.getStockQuantity());
    }

    @Override
    @Transactional
    public void increaseStock(Long productId, int quantity) {
        log.debug("재고 증가 - 상품 ID: {}, 증가 수량: {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));
        
        int originalStock = product.getStockQuantity();
        product.setStockQuantity(originalStock + quantity);
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepository.save(product);
        
        log.info("재고 증가 완료 - 상품 ID: {}, 기존 재고: {}, 증가 수량: {}, 현재 재고: {}", 
                 productId, originalStock, quantity, product.getStockQuantity());
    }

    // === 관리자 기능 ===

    @Override
    @Transactional
    public ProductDto.DetailResponse createProduct(ProductDto.CreateRequest request) {
        log.debug("상품 생성 - 상품명: {}", request.getName());
        
        Product product = request.toEntity();
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        
        log.info("상품 생성 완료 - ID: {}, 상품명: {}", savedProduct.getId(), savedProduct.getName());
        return ProductDto.DetailResponse.from(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto.DetailResponse updateProduct(Long id, ProductDto.UpdateRequest request) {
        log.debug("상품 수정 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + id));
        
        // 업데이트 로직 (request의 필드들을 product에 적용)
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getIsNew() != null) {
            product.setIsNew(request.getIsNew());
        }
        if (request.getHasEvent() != null) {
            product.setHasEvent(request.getHasEvent());
        }
        
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        
        log.info("상품 수정 완료 - ID: {}, 상품명: {}", savedProduct.getId(), savedProduct.getName());
        return ProductDto.DetailResponse.from(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.debug("상품 삭제 - ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + id));
        
        productRepository.delete(product);
        
        log.info("상품 삭제 완료 - ID: {}, 상품명: {}", id, product.getName());
    }

    // === 헬퍼 메서드 ===

    /**
     * 정렬 조건 생성
     */
    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? 
                                  Sort.Direction.ASC : Sort.Direction.DESC;
        
        // 허용된 정렬 필드만 사용
        switch (sortBy.toLowerCase()) {
            case "price":
                return Sort.by(direction, "price");
            case "name":
                return Sort.by(direction, "name");
            case "rating":
                return Sort.by(direction, "averageRating");
            case "createdat":
            case "created_at":
            default:
                return Sort.by(direction, "createdAt");
        }
    }
}