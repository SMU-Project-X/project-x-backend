package com.pix.controller;

import com.pix.entity.Category;
import com.pix.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController {

    private final CategoryRepository categoryRepository;

    /**
     * 전체 카테고리 목록 조회 (ID 순서대로)
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("카테고리 목록 조회 요청");
        
        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(categories);
    }

    /**
     * 카테고리별 상품 수와 함께 조회
     */
    @GetMapping("/with-product-count")
    public ResponseEntity<List<Map<String, Object>>> getCategoriesWithProductCount() {
        log.info("카테고리별 상품 수 조회 요청");
        
        List<Object[]> results = categoryRepository.findCategoriesWithProductCount();
        
        List<Map<String, Object>> categoryData = results.stream()
                .map(result -> Map.of(
                        "id", result[0],
                        "name", result[1],
                        "productCount", result[2]
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categoryData);
    }

    /**
     * 상품이 있는 카테고리만 조회
     */
    @GetMapping("/with-products")
    public ResponseEntity<List<Category>> getCategoriesWithProducts() {
        log.info("상품이 있는 카테고리 조회 요청");
        
        List<Category> categories = categoryRepository.findCategoriesWithProducts();
        return ResponseEntity.ok(categories);
    }

    /**
     * 카테고리 ID로 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        log.info("카테고리 조회 요청 - ID: {}", id);
        
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 카테고리명으로 조회
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        log.info("카테고리 조회 요청 - 이름: {}", name);
        
        return categoryRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 카테고리 검색 (부분 일치)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String keyword) {
        log.info("카테고리 검색 요청 - 키워드: {}", keyword);
        
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCaseOrderByNameAsc(keyword);
        return ResponseEntity.ok(categories);
    }

    // ===== 관리자용 API =====

    /**
     * 카테고리 생성 (관리자용)
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        log.info("카테고리 생성 요청 - 이름: {}", category.getName());
        
        try {
            // 카테고리명 중복 체크
            if (categoryRepository.existsByName(category.getName())) {
                log.warn("카테고리명 중복 - 이름: {}", category.getName());
                return ResponseEntity.badRequest().build();
            }
            
            Category savedCategory = categoryRepository.save(category);
            log.info("카테고리 생성 완료 - ID: {}, 이름: {}", savedCategory.getId(), savedCategory.getName());
            
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            log.error("카테고리 생성 실패 - 이름: {}, 원인: {}", category.getName(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 카테고리 수정 (관리자용)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryRequest) {
        log.info("카테고리 수정 요청 - ID: {}", id);
        
        return categoryRepository.findById(id)
                .map(category -> {
                    try {
                        // 카테고리명 중복 체크 (자신 제외)
                        if (categoryRequest.getName() != null && 
                            !categoryRequest.getName().equals(category.getName()) &&
                            categoryRepository.existsByNameAndIdNot(categoryRequest.getName(), id)) {
                            log.warn("카테고리명 중복 - 이름: {}", categoryRequest.getName());
                            return ResponseEntity.badRequest().<Category>build();
                        }
                        
                        // 필드 업데이트 (null이 아닌 값만)
                        if (categoryRequest.getName() != null) {
                            category.setName(categoryRequest.getName());
                        }
                        
                        Category updatedCategory = categoryRepository.save(category);
                        log.info("카테고리 수정 완료 - ID: {}, 이름: {}", updatedCategory.getId(), updatedCategory.getName());
                        
                        return ResponseEntity.ok(updatedCategory);
                    } catch (Exception e) {
                        log.error("카테고리 수정 실패 - ID: {}, 원인: {}", id, e.getMessage());
                        return ResponseEntity.badRequest().<Category>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 카테고리 삭제 (관리자용) - 실제 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("카테고리 삭제 요청 - ID: {}", id);
        
        return categoryRepository.findById(id)
                .map(category -> {
                    // 해당 카테고리를 사용하는 상품이 있는지 확인
                    Long productCount = categoryRepository.countByCategoryId(id);
                    if (productCount > 0) {
                        log.warn("카테고리 삭제 실패 - 사용 중인 상품 존재 - ID: {}, 상품 수: {}", id, productCount);
                        return ResponseEntity.badRequest().<Void>build();
                    }
                    
                    categoryRepository.delete(category);
                    
                    log.info("카테고리 삭제 완료 - ID: {}, 이름: {}", category.getId(), category.getName());
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 카테고리 통계 조회
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCategoryStatistics() {
        log.info("카테고리 통계 조회 요청");
        
        Long totalCategories = categoryRepository.countAllCategories();
        Long categoriesWithProducts = categoryRepository.countCategoriesWithProducts();
        
        Map<String, Object> statistics = Map.of(
                "totalCategories", totalCategories,
                "categoriesWithProducts", categoriesWithProducts,
                "categoriesWithoutProducts", totalCategories - categoriesWithProducts
        );
        
        return ResponseEntity.ok(statistics);
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
        return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
    }
}