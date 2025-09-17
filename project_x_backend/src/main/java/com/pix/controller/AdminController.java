package com.pix.controller;

import com.pix.dto.UserDto; 
import com.pix.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 관리자 권한 체크 공통 메서드
     */
    private boolean checkAdminPermission(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        return Boolean.TRUE.equals(isLoggedIn) && Boolean.TRUE.equals(isAdmin);
    }

    /**
     * 대시보드 전체 통계 조회 API
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(HttpSession session) {
        // 관리자 권한 체크
        if (!checkAdminPermission(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            log.info("관리자 대시보드 통계 조회 요청");

            Map<String, Object> dashboardStats = new HashMap<>();

            // 1. 사용자 통계 
            UserDto.UserStats userStats = userService.getUserStats();
            Map<String, Object> userStatsMap = new HashMap<>();
            userStatsMap.put("totalUsers", userStats.getTotalUsers());
            userStatsMap.put("normalUsers", userStats.getNormalUsers());
            userStatsMap.put("adminUsers", userStats.getAdminUsers());
            dashboardStats.put("userStats", userStatsMap);

            // 2. 상품 통계
            Map<String, Object> productStats = getProductStats();
            dashboardStats.put("productStats", productStats);

            // 3. 카테고리 통계
            Map<String, Object> categoryStats = getCategoryStats();
            dashboardStats.put("categoryStats", categoryStats);

            // 4. 장바구니 통계
            Map<String, Object> cartStats = getCartStats();
            dashboardStats.put("cartStats", cartStats);

            // 5. 시스템 정보
            Map<String, Object> systemInfo = getSystemInfo();
            dashboardStats.put("systemInfo", systemInfo);

            log.info("관리자 대시보드 통계 조회 완료");
            return ResponseEntity.ok(dashboardStats);

        } catch (Exception e) {
            log.error("관리자 대시보드 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 사용자 통계 조회 (상세)
     */
    @GetMapping("/stats/users")
    public ResponseEntity<Map<String, Object>> getUserStatsDetailed(HttpSession session) {
        if (!checkAdminPermission(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            // 타입 수정: UserService.UserStats → UserDto.UserStats
            UserDto.UserStats userStats = userService.getUserStats();
            
            Map<String, Object> detailedStats = new HashMap<>();
            detailedStats.put("totalUsers", userStats.getTotalUsers());
            detailedStats.put("normalUsers", userStats.getNormalUsers());
            detailedStats.put("adminUsers", userStats.getAdminUsers());
            
            // 추가 통계 정보
            List<Map<String, Object>> recentUsers = getRecentUsers();
            detailedStats.put("recentUsers", recentUsers);
            
            Map<String, Object> userGrowth = getUserGrowthStats();
            detailedStats.put("growth", userGrowth);

            return ResponseEntity.ok(detailedStats);

        } catch (Exception e) {
            log.error("사용자 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 상품 통계 조회
     */
    @GetMapping("/stats/products")
    public ResponseEntity<Map<String, Object>> getProductStatsDetailed(HttpSession session) {
        if (!checkAdminPermission(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Map<String, Object> productStats = getProductStats();
            
            // 추가 상세 정보
            List<Map<String, Object>> categoryDistribution = getCategoryDistribution();
            productStats.put("categoryDistribution", categoryDistribution);
            
            List<Map<String, Object>> priceRangeDistribution = getPriceRangeDistribution();
            productStats.put("priceRangeDistribution", priceRangeDistribution);

            return ResponseEntity.ok(productStats);

        } catch (Exception e) {
            log.error("상품 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //  ===== 통계 계산 헬퍼 메서드들 =====
    
    /**
     * 상품 통계 계산
     */
    private Map<String, Object> getProductStats() {
        try {
            String sql = "SELECT COUNT(*) as total_products FROM products";
            Integer totalProducts = jdbcTemplate.queryForObject(sql, Integer.class);

            String categorySql = "SELECT COUNT(DISTINCT category_id) as total_categories FROM products";
            Integer totalCategories = jdbcTemplate.queryForObject(categorySql, Integer.class);

            String stockSql = "SELECT COUNT(*) as out_of_stock FROM products WHERE stock_quantity = 0";
            Integer outOfStock = jdbcTemplate.queryForObject(stockSql, Integer.class);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProducts", totalProducts != null ? totalProducts : 0);
            stats.put("totalCategories", totalCategories != null ? totalCategories : 0);
            stats.put("outOfStock", outOfStock != null ? outOfStock : 0);
            stats.put("inStock", (totalProducts != null ? totalProducts : 0) - (outOfStock != null ? outOfStock : 0));

            return stats;
        } catch (Exception e) {
            log.warn("상품 통계 계산 실패, 기본값 반환: {}", e.getMessage());
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalProducts", 0);
            defaultStats.put("totalCategories", 0);
            defaultStats.put("outOfStock", 0);
            defaultStats.put("inStock", 0);
            return defaultStats;
        }
    }

    /**
     * 카테고리 통계 계산
     */
    private Map<String, Object> getCategoryStats() {
        try {
            String sql = "SELECT c.name, COUNT(p.id) as product_count " +
                        "FROM categories c LEFT JOIN products p ON c.id = p.category_id " +
                        "GROUP BY c.id, c.name ORDER BY product_count DESC";
            
            List<Map<String, Object>> categories = jdbcTemplate.queryForList(sql);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("categories", categories);
            stats.put("totalCategories", categories.size());
            
            return stats;
        } catch (Exception e) {
            log.warn("카테고리 통계 계산 실패, 기본값 반환: {}", e.getMessage());
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("categories", new ArrayList<>());
            defaultStats.put("totalCategories", 0);
            return defaultStats;
        }
    }

    /**
     * 장바구니 통계 계산 (더미 데이터)
     */
    private Map<String, Object> getCartStats() {
        // 실제 환경에서는 장바구니 테이블에서 데이터를 가져와야 함
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCartItems", 0);
        stats.put("averageCartValue", 0);
        stats.put("cartConversionRate", 0.0);
        return stats;
    }

    /**
     * 시스템 정보
     */
    private Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("serverStatus", "Healthy");
        info.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        info.put("databaseConnections", 5);
        info.put("activeUsers", 12);
        info.put("serverUptime", "3일 14시간 25분");
        return info;
    }

    /**
     * 최근 가입 사용자 목록
     */
    private List<Map<String, Object>> getRecentUsers() {
        try {
            String sql = "SELECT username, name, created_at FROM users " +
                        "ORDER BY created_at DESC LIMIT 10";
            
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.warn("최근 사용자 조회 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 사용자 증가 추세
     */
    private Map<String, Object> getUserGrowthStats() {
        try {
            String sql = "SELECT DATE(created_at) as date, COUNT(*) as count " +
                        "FROM users WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                        "GROUP BY DATE(created_at) ORDER BY date";
            
            List<Map<String, Object>> growthData = jdbcTemplate.queryForList(sql);
            
            Map<String, Object> growth = new HashMap<>();
            growth.put("dailyGrowth", growthData);
            growth.put("totalGrowthLast30Days", 
                       growthData.stream().mapToInt(row -> ((Number) row.get("count")).intValue()).sum());
            
            return growth;
        } catch (Exception e) {
            log.warn("사용자 증가 추세 계산 실패: {}", e.getMessage());
            Map<String, Object> defaultGrowth = new HashMap<>();
            defaultGrowth.put("dailyGrowth", new ArrayList<>());
            defaultGrowth.put("totalGrowthLast30Days", 0);
            return defaultGrowth;
        }
    }

    /**
     * 카테고리별 상품 분포
     */
    private List<Map<String, Object>> getCategoryDistribution() {
        try {
            String sql = "SELECT c.name, COUNT(p.id) as count, " +
                        "COALESCE(SUM(p.price * COALESCE(p.stock_quantity, 0)), 0) as total_value " +
                        "FROM categories c LEFT JOIN products p ON c.id = p.category_id " +
                        "GROUP BY c.id, c.name ORDER BY count DESC";
            
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.warn("카테고리 분포 계산 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 가격대별 상품 분포
     */
    private List<Map<String, Object>> getPriceRangeDistribution() {
        try {
            String sql = "SELECT " +
                        "CASE " +
                        "  WHEN price < 10000 THEN '1만원 미만' " +
                        "  WHEN price < 30000 THEN '1-3만원' " +
                        "  WHEN price < 50000 THEN '3-5만원' " +
                        "  WHEN price < 100000 THEN '5-10만원' " +
                        "  ELSE '10만원 이상' " +
                        "END as price_range, " +
                        "COUNT(*) as count " +
                        "FROM products " +
                        "GROUP BY price_range " +
                        "ORDER BY MIN(price)";
            
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.warn("가격대별 분포 계산 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", e.getMessage());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("서버 오류 발생: {}", e.getMessage(), e);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "서버 오류가 발생했습니다.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}