package com.pix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 1) CORS: 프론트엔드에서 오는 모든 요청 허용 (403 에러 해결)
 * 2) 정적 리소스 매핑: /files/** → 파일시스템(app.upload-dir) 경로 노출
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties에 설정한 업로드 루트 디렉토리 경로
    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 🔧 모든 경로에 CORS 적용 (API뿐만 아니라 전체)
                .allowedOriginPatterns("*") // 🔧 모든 Origin 허용 (개발환경)
                .allowedOrigins(
                    // 🔧 React/Vite 개발 서버 포트들 모두 추가
                    "http://localhost:3000",    // React 기본 포트
                    "http://localhost:5173",    // Vite 기본 포트
                    "http://127.0.0.1:3000",    // 로컬호스트 대체
                    "http://127.0.0.1:5173",
                    // 🔧 추가 포트들 (필요시)
                    "http://localhost:3001",
                    "http://localhost:8080"     // 백엔드 자체 호출용
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 🔧 모든 HTTP 메서드
                .allowedHeaders("*") // 🔧 모든 헤더 허용
                .exposedHeaders("*") // 🔧 모든 응답 헤더 노출
                .allowCredentials(true) // 🔧 쿠키/인증 정보 허용
                .maxAge(3600); // 🔧 Preflight 캐시 시간
    }

    // 🔧 추가 CORS 설정 (더 강력한 방법)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 🔧 모든 Origin 허용 (개발환경)
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://127.0.0.1:3000");
        configuration.addAllowedOrigin("http://127.0.0.1:5173");
        
        // 🔧 모든 HTTP 메서드 허용
        configuration.addAllowedMethod("*");
        
        // 🔧 모든 헤더 허용
        configuration.addAllowedHeader("*");
        
        // 🔧 쿠키 및 인증 정보 허용
        configuration.setAllowCredentials(true);
        
        // 🔧 Preflight 캐시 시간
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 🔧 모든 경로에 적용
        
        return source;
    }

    // 응원봉. 유저가 스티커 이미지 파일 업로드 시 저장 경로
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 예) app.upload-dir=./uploads/stickers → 절대경로로 변환 후 /files/** 에 매핑
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString() + "/";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + absolutePath);
        
        // 🔧 추가 정적 리소스 경로들 (필요시)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
                
        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/public/");
                
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}