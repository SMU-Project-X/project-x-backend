package com.pix.repository;

import com.pix.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 이름으로 카테고리 조회
    Optional<Category> findByName(String name);

    // 카테고리명 중복 확인
    boolean existsByName(String name);

    // 다른 카테고리에서 같은 이름 사용 여부 확인 (수정 시 사용)
    boolean existsByNameAndIdNot(String name, Long id);

    // 이름으로 부분 검색
    List<Category> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    // 모든 카테고리 조회 (ID 순서)
    List<Category> findAllByOrderByIdAsc();

    // 카테고리명 목록 조회 (중복 제거, 정렬)
    @Query("SELECT DISTINCT c.name FROM Category c ORDER BY c.name")
    List<String> findDistinctNames();

    // 카테고리별 상품 수 조회
    @Query("SELECT c.id, c.name, COUNT(p.id) " +
           "FROM Category c LEFT JOIN Product p ON p.categoryId = c.id " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.id")
    List<Object[]> findCategoriesWithProductCount();

    // 상품이 있는 카테고리만 조회
    @Query("SELECT DISTINCT c FROM Category c " +
           "WHERE EXISTS (SELECT 1 FROM Product p WHERE p.categoryId = c.id) " +
           "ORDER BY c.id")
    List<Category> findCategoriesWithProducts();

    // 특정 ID들로 카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.id IN :ids ORDER BY c.id")
    List<Category> findByIdIn(@Param("ids") List<Long> ids);

    // 카테고리 통계
    @Query("SELECT COUNT(c) FROM Category c")
    Long countAllCategories();

    // 상품이 있는 카테고리 수
    @Query("SELECT COUNT(DISTINCT c.id) FROM Category c " +
           "WHERE EXISTS (SELECT 1 FROM Product p WHERE p.categoryId = c.id)")
    Long countCategoriesWithProducts();

    // 특정 카테고리를 사용하는 상품 수 조회 (삭제 전 체크용)
    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    Long countByCategoryId(@Param("categoryId") Long categoryId);

    // 카테고리별 평균 상품 가격 조회
    @Query("SELECT c.id, c.name, AVG(p.price) " +
           "FROM Category c LEFT JOIN Product p ON p.categoryId = c.id " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.id")
    List<Object[]> findCategoriesWithAveragePrice();

    // 카테고리별 최대/최소 상품 가격 조회
    @Query("SELECT c.id, c.name, MIN(p.price), MAX(p.price) " +
           "FROM Category c LEFT JOIN Product p ON p.categoryId = c.id " +
           "GROUP BY c.id, c.name " +
           "HAVING COUNT(p.id) > 0 " +
           "ORDER BY c.id")
    List<Object[]> findCategoriesWithPriceRange();

    // 빈 카테고리 조회 (상품이 없는 카테고리)
    @Query("SELECT c FROM Category c " +
           "WHERE NOT EXISTS (SELECT 1 FROM Product p WHERE p.categoryId = c.id) " +
           "ORDER BY c.id")
    List<Category> findEmptyCategories();
}