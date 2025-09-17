package com.pix.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ExchangeController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);
    
    // 환율 API 설정
    private static final String EXCHANGE_API_BASE_URL = "https://api.exchangerate-api.com/v4/latest";
    private static final String BACKUP_API_URL = "https://open.er-api.com/v6/latest";
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // 지원 통화 목록
    private final Map<String, String> SUPPORTED_CURRENCIES = createSupportedCurrencies();
    
    private static Map<String, String> createSupportedCurrencies() {
        Map<String, String> currencies = new HashMap<>();
        currencies.put("KRW", "대한민국 원 (₩)");
        currencies.put("USD", "미국 달러 ($)");
        currencies.put("EUR", "유로 (€)");
        currencies.put("JPY", "일본 엔 (¥)");
        currencies.put("GBP", "영국 파운드 (£)");
        currencies.put("CNY", "중국 위안 (¥)");
        currencies.put("AUD", "호주 달러 (A$)");
        currencies.put("CAD", "캐나다 달러 (C$)");
        currencies.put("CHF", "스위스 프랑 (CHF)");
        currencies.put("SEK", "스웨덴 크로나 (kr)");
        currencies.put("NOK", "노르웨이 크로네 (kr)");
        currencies.put("DKK", "덴마크 크로네 (kr)");
        currencies.put("PLN", "폴란드 즐로티 (zł)");
        currencies.put("CZK", "체코 코루나 (Kč)");
        currencies.put("HUF", "헝가리 포린트 (Ft)");
        currencies.put("RUB", "러시아 루블 (₽)");
        return currencies;
    }
    
    // 폴백 환율 데이터
    private final Map<String, Map<String, Double>> FALLBACK_RATES = createFallbackRates();
    
    private static Map<String, Map<String, Double>> createFallbackRates() {
        Map<String, Map<String, Double>> fallbackRates = new HashMap<>();
        
        // KRW 기준 환율
        Map<String, Double> krwRates = new HashMap<>();
        krwRates.put("USD", 0.00073);
        krwRates.put("EUR", 0.00070);
        krwRates.put("JPY", 0.11);
        krwRates.put("GBP", 0.00058);
        krwRates.put("CNY", 0.0053);
        krwRates.put("AUD", 0.0011);
        krwRates.put("CAD", 0.0010);
        krwRates.put("CHF", 0.00066);
        fallbackRates.put("KRW", krwRates);
        
        // USD 기준 환율
        Map<String, Double> usdRates = new HashMap<>();
        usdRates.put("KRW", 1370.0);
        usdRates.put("EUR", 0.95);
        usdRates.put("JPY", 150.0);
        usdRates.put("GBP", 0.79);
        usdRates.put("CNY", 7.2);
        usdRates.put("AUD", 1.50);
        usdRates.put("CAD", 1.35);
        usdRates.put("CHF", 0.90);
        fallbackRates.put("USD", usdRates);
        
        return fallbackRates;
    }
    
    /**
     * 지원 통화 목록 반환
     */
    @GetMapping("/currencies")
    public ResponseEntity<Map<String, Object>> getSupportedCurrencies() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", SUPPORTED_CURRENCIES);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("지원 통화 목록 조회 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "지원 통화 목록을 가져올 수 없습니다.");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 환율 정보 조회 (기본 통화 기준)
     */
    @GetMapping("/rates/{baseCurrency}")
    public ResponseEntity<Map<String, Object>> getExchangeRates(@PathVariable String baseCurrency) {
        try {
            logger.info("환율 정보 조회 요청: baseCurrency={}", baseCurrency);
            
            if (!StringUtils.hasText(baseCurrency)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "기준 통화가 지정되지 않았습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            baseCurrency = baseCurrency.toUpperCase();
            
            try {
                // 메인 API 호출
                String url = EXCHANGE_API_BASE_URL + "/" + baseCurrency;
                
                @SuppressWarnings("unchecked")
                Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);
                
                if (apiResponse != null && apiResponse.containsKey("rates")) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", Map.of(
                        "base", baseCurrency,
                        "rates", apiResponse.get("rates"),
                        "lastUpdated", apiResponse.get("date"),
                        "fromCache", false
                    ));
                    
                    logger.info("환율 정보 조회 성공: {}", baseCurrency);
                    return ResponseEntity.ok(response);
                }
                
            } catch (HttpClientErrorException e) {
                logger.warn("메인 환율 API 실패, 백업 API 시도: {}", e.getMessage());
                
                try {
                    // 백업 API 호출
                    String backupUrl = BACKUP_API_URL + "/" + baseCurrency;
                    
                    @SuppressWarnings("unchecked")
                    Map<String, Object> backupResponse = restTemplate.getForObject(backupUrl, Map.class);
                    
                    if (backupResponse != null && backupResponse.containsKey("rates")) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("data", Map.of(
                            "base", baseCurrency,
                            "rates", backupResponse.get("rates"),
                            "lastUpdated", backupResponse.get("time_last_update_utc"),
                            "fromCache", false,
                            "warning", "백업 API를 사용했습니다."
                        ));
                        
                        logger.info("백업 환율 API 성공: {}", baseCurrency);
                        return ResponseEntity.ok(response);
                    }
                    
                } catch (Exception backupError) {
                    logger.error("백업 환율 API도 실패: {}", backupError.getMessage());
                }
            }
            
            // API 실패시 폴백 데이터 사용
            Map<String, Double> fallbackRates = FALLBACK_RATES.getOrDefault(baseCurrency, Map.of());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "환율 API 연결 실패");
            response.put("data", Map.of(
                "base", baseCurrency,
                "rates", fallbackRates,
                "lastUpdated", "2025-01-01",
                "fromCache", false,
                "isFallback", true,
                "warning", "환율 API에 연결할 수 없어 참고용 환율을 사용합니다."
            ));
            
            logger.warn("폴백 환율 데이터 사용: {}", baseCurrency);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("환율 정보 조회 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "환율 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 통화 변환 (특정 금액을 다른 통화로 변환)
     */
    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertCurrency(@RequestBody Map<String, Object> request) {
        try {
            logger.info("통화 변환 요청: {}", request);
            
            Double amount = null;
            Object amountObj = request.get("amount");
            if (amountObj instanceof Number) {
                amount = ((Number) amountObj).doubleValue();
            }
            
            String fromCurrency = (String) request.get("fromCurrency");
            String toCurrency = (String) request.get("toCurrency");
            
            // 유효성 검사
            if (amount == null || amount <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "올바른 금액을 입력해주세요.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (!StringUtils.hasText(fromCurrency) || !StringUtils.hasText(toCurrency)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "변환할 통화를 지정해주세요.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            fromCurrency = fromCurrency.toUpperCase();
            toCurrency = toCurrency.toUpperCase();
            
            // 같은 통화면 그대로 반환
            if (fromCurrency.equals(toCurrency)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Map.of(
                    "originalAmount", amount,
                    "convertedAmount", amount,
                    "fromCurrency", fromCurrency,
                    "toCurrency", toCurrency,
                    "rate", 1.0
                ));
                return ResponseEntity.ok(response);
            }
            
            // 환율 정보 가져오기
            String ratesUrl = EXCHANGE_API_BASE_URL + "/" + fromCurrency;
            
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> ratesResponse = restTemplate.getForObject(ratesUrl, Map.class);
                
                if (ratesResponse != null && ratesResponse.containsKey("rates")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Double> rates = (Map<String, Double>) ratesResponse.get("rates");
                    
                    Double rate = rates.get(toCurrency);
                    if (rate == null) {
                        throw new RuntimeException(toCurrency + " 환율을 찾을 수 없습니다.");
                    }
                    
                    // 변환 계산 (소수점 2자리까지)
                    BigDecimal convertedAmount = BigDecimal.valueOf(amount)
                        .multiply(BigDecimal.valueOf(rate))
                        .setScale(2, RoundingMode.HALF_UP);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", Map.of(
                        "originalAmount", amount,
                        "convertedAmount", convertedAmount.doubleValue(),
                        "fromCurrency", fromCurrency,
                        "toCurrency", toCurrency,
                        "rate", rate,
                        "lastUpdated", ratesResponse.get("date")
                    ));
                    
                    logger.info("통화 변환 성공: {} {} -> {} {}", 
                               amount, fromCurrency, convertedAmount, toCurrency);
                    
                    return ResponseEntity.ok(response);
                }
                
            } catch (Exception e) {
                logger.error("환율 API 호출 실패: {}", e.getMessage());
                
                // 폴백 환율 사용
                Map<String, Double> fallbackRates = FALLBACK_RATES.get(fromCurrency);
                if (fallbackRates != null && fallbackRates.containsKey(toCurrency)) {
                    Double fallbackRate = fallbackRates.get(toCurrency);
                    BigDecimal convertedAmount = BigDecimal.valueOf(amount)
                        .multiply(BigDecimal.valueOf(fallbackRate))
                        .setScale(2, RoundingMode.HALF_UP);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", Map.of(
                        "originalAmount", amount,
                        "convertedAmount", convertedAmount.doubleValue(),
                        "fromCurrency", fromCurrency,
                        "toCurrency", toCurrency,
                        "rate", fallbackRate,
                        "warning", "참고용 환율을 사용했습니다."
                    ));
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            // 모든 시도가 실패한 경우
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "환율 변환을 수행할 수 없습니다.");
            errorResponse.put("data", Map.of(
                "originalAmount", amount,
                "convertedAmount", amount,
                "fromCurrency", fromCurrency,
                "toCurrency", toCurrency,
                "rate", 1.0
            ));
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("통화 변환 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "통화 변환 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 일괄 가격 변환 (여러 가격을 한 번에 변환)
     */
    @PostMapping("/convert-batch")
    public ResponseEntity<Map<String, Object>> convertBatch(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> amountObjects = (List<Object>) request.get("amounts");
            String fromCurrency = (String) request.get("fromCurrency");
            String toCurrency = (String) request.get("toCurrency");
            
            // Object를 Double로 안전하게 변환
            List<Double> amounts = new ArrayList<>();
            if (amountObjects != null) {
                for (Object obj : amountObjects) {
                    if (obj instanceof Number) {
                        amounts.add(((Number) obj).doubleValue());
                    } else {
                        logger.warn("유효하지 않은 금액 데이터: {}", obj);
                    }
                }
            }
            
            logger.info("일괄 통화 변환 요청: {} -> {}, 가격 개수: {}", 
                       fromCurrency, toCurrency, amounts.size());
            
            if (amounts == null || amounts.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Map.of("conversions", List.of()));
                return ResponseEntity.ok(response);
            }
            
            // 환율 정보를 한 번만 가져옴
            String ratesUrl = EXCHANGE_API_BASE_URL + "/" + fromCurrency.toUpperCase();
            
            @SuppressWarnings("unchecked")
            Map<String, Object> ratesResponse = restTemplate.getForObject(ratesUrl, Map.class);
            
            if (ratesResponse == null || !ratesResponse.containsKey("rates")) {
                throw new RuntimeException("환율 정보를 가져올 수 없습니다.");
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Double> rates = (Map<String, Double>) ratesResponse.get("rates");
            Double rate = rates.get(toCurrency.toUpperCase());
            
            if (rate == null) {
                throw new RuntimeException(toCurrency + " 환율을 찾을 수 없습니다.");
            }
            
            // 모든 금액 변환
            List<Map<String, Object>> conversions = amounts.stream()
                .map(amount -> {
                    BigDecimal convertedAmount = BigDecimal.valueOf(amount)
                        .multiply(BigDecimal.valueOf(rate))
                        .setScale(2, RoundingMode.HALF_UP);
                    
                    Map<String, Object> conversion = new HashMap<>();
                    conversion.put("originalAmount", amount);
                    conversion.put("convertedAmount", convertedAmount.doubleValue());
                    conversion.put("rate", rate);
                    return conversion;
                })
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                "conversions", conversions,
                "fromCurrency", fromCurrency.toUpperCase(),
                "toCurrency", toCurrency.toUpperCase(),
                "rate", rate,
                "lastUpdated", ratesResponse.get("date")
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("일괄 통화 변환 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "일괄 통화 변환 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 헬스체크 API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Exchange API is running");
        response.put("supportedCurrencies", SUPPORTED_CURRENCIES.size());
        return ResponseEntity.ok(response);
    }
}