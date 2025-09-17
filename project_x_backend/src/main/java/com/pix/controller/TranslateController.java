package com.pix.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/translate")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TranslateController {
    
    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class);
    
    // DeepL API 설정
    private static final String DEEPL_API_BASE_URL = "https://api-free.deepl.com/v2";
    
    @Value("${deepl.api.key:a4465cef-1066-48f2-81e6-1cd67ab1d25b:fx}")
    private String deeplApiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 지원되는 언어 목록 반환
     */
    @GetMapping("/languages")
    public ResponseEntity<Map<String, Object>> getSupportedLanguages() {
        try {
            Map<String, String> languages = new HashMap<>();
            languages.put("ko", "한국어");
            languages.put("en", "English");
            languages.put("ja", "日本語");
            languages.put("zh", "中文");
            languages.put("es", "Español");
            languages.put("fr", "Français");
            languages.put("de", "Deutsch");
            languages.put("it", "Italiano");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", languages);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("지원 언어 목록 조회 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "지원 언어 목록을 가져올 수 없습니다.");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 단일 텍스트 번역
     */
    @PostMapping("/text")
    public ResponseEntity<Map<String, Object>> translateText(@RequestBody Map<String, Object> request) {
        try {
            String text = (String) request.get("text");
            String sourceLang = (String) request.get("sourceLang");
            String targetLang = (String) request.get("targetLang");
            
            logger.info("번역 요청: {} -> {}, 텍스트 길이: {}", sourceLang, targetLang, 
                       text != null ? text.length() : 0);
            
            // 유효성 검사
            if (!StringUtils.hasText(text)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Map.of("translatedText", ""));
                return ResponseEntity.ok(response);
            }
            
            if (!StringUtils.hasText(targetLang)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "대상 언어가 지정되지 않았습니다.");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 같은 언어면 번역하지 않음
            if (sourceLang != null && sourceLang.equals(targetLang)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Map.of("translatedText", text));
                return ResponseEntity.ok(response);
            }
            
            // API 키 확인
            if (!StringUtils.hasText(deeplApiKey)) {
                logger.warn("DeepL API 키가 설정되지 않았습니다.");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "번역 서비스를 사용할 수 없습니다.");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            }
            
            // 텍스트 길이 제한
            if (text.length() > 5000) {
                text = text.substring(0, 5000);
                logger.warn("텍스트가 5000자로 제한되었습니다.");
            }
            
            // 언어 코드 변환
            String sourceCode = mapLanguageCode(sourceLang);
            String targetCode = mapLanguageCode(targetLang);
            
            // DeepL API 호출
            Map<String, Object> translationResult = callDeepLAPI(text, sourceCode, targetCode);
            
            return ResponseEntity.ok(translationResult);
            
        } catch (Exception e) {
            logger.error("텍스트 번역 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "번역 중 오류가 발생했습니다: " + e.getMessage());
            
            // 실패시 원본 텍스트 반환
            String originalText = (String) request.get("text");
            errorResponse.put("data", Map.of("translatedText", originalText != null ? originalText : ""));
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 배치 번역 (여러 텍스트를 한 번에 번역)
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> translateBatch(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> texts = (List<String>) request.get("texts");
            String sourceLang = (String) request.get("sourceLang");
            String targetLang = (String) request.get("targetLang");
            
            logger.info("배치 번역 요청: {} -> {}, 텍스트 개수: {}", sourceLang, targetLang,
                       texts != null ? texts.size() : 0);
            
            if (texts == null || texts.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Map.of("translations", List.of()));
                return ResponseEntity.ok(response);
            }
            
            // 각 텍스트를 개별 번역 (DeepL 무료 버전 제한 고려)
            List<Map<String, Object>> translations = texts.stream()
                .map(text -> {
                    try {
                        if (!StringUtils.hasText(text)) {
                            return Map.of("success", true, "data", Map.of("translatedText", ""));
                        }
                        
                        String sourceCode = mapLanguageCode(sourceLang);
                        String targetCode = mapLanguageCode(targetLang);
                        
                        return callDeepLAPI(text.substring(0, Math.min(text.length(), 1000)), sourceCode, targetCode);
                        
                    } catch (Exception e) {
                        logger.error("개별 텍스트 번역 실패: {}", e.getMessage());
                        return Map.of(
                            "success", false,
                            "error", e.getMessage(),
                            "data", Map.of("translatedText", text)
                        );
                    }
                })
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of("translations", translations));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("배치 번역 중 오류 발생", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "배치 번역 중 오류가 발생했습니다: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * DeepL API 호출
     */
    private Map<String, Object> callDeepLAPI(String text, String sourceLang, String targetLang) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "DeepL-Auth-Key " + deeplApiKey);
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("text", text);
            body.add("target_lang", targetLang);
            if (StringUtils.hasText(sourceLang)) {
                body.add("source_lang", sourceLang);
            }
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                DEEPL_API_BASE_URL + "/translate", 
                entity, 
                Map.class
            );
            
            if (response != null && response.containsKey("translations")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> translations = (List<Map<String, Object>>) response.get("translations");
                
                if (!translations.isEmpty()) {
                    Map<String, Object> translation = translations.get(0);
                    String translatedText = (String) translation.get("text");
                    String detectedSourceLang = (String) translation.get("detected_source_language");
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("data", Map.of(
                        "translatedText", translatedText,
                        "detectedSourceLang", detectedSourceLang != null ? detectedSourceLang : sourceLang
                    ));
                    
                    return result;
                }
            }
            
            throw new RuntimeException("DeepL API 응답이 올바르지 않습니다.");
            
        } catch (HttpClientErrorException e) {
            logger.error("DeepL API 클라이언트 에러: {}", e.getMessage());
            
            if (e.getStatusCode().value() == 403) {
                throw new RuntimeException("DeepL API 키가 유효하지 않습니다.");
            } else if (e.getStatusCode().value() == 429) {
                throw new RuntimeException("DeepL API 사용량 한도를 초과했습니다.");
            } else {
                throw new RuntimeException("DeepL API 호출 실패: " + e.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("DeepL API 호출 중 예외 발생", e);
            throw new RuntimeException("번역 서비스 오류: " + e.getMessage());
        }
    }
    
    /**
     * 언어 코드 매핑 (프론트엔드 코드 -> DeepL API 코드)
     */
    private String mapLanguageCode(String languageCode) {
        if (!StringUtils.hasText(languageCode)) {
            return null;
        }
        
        Map<String, String> codeMap = Map.of(
            "ko", "KO",
            "en", "EN",
            "ja", "JA",
            "zh", "ZH",
            "es", "ES",
            "fr", "FR",
            "de", "DE",
            "it", "IT"
        );
        
        return codeMap.getOrDefault(languageCode.toLowerCase(), languageCode.toUpperCase());
    }
    
    /**
     * 헬스체크 API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Translation API is running");
        response.put("apiKeyConfigured", StringUtils.hasText(deeplApiKey));
        return ResponseEntity.ok(response);
    }
}