// src/main/java/com/pix/config/WebConfig.java
package com.pix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 경로에 대해 CORS 설정
                .allowedOrigins(
                    "http://localhost:3000",    // Vite 개발 서버 (기본 포트)
                    "http://localhost:5173",    // Vite 개발 서버 (대체 포트)
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)  // 세션 쿠키 허용
                .maxAge(3600); // preflight 요청 캐시 시간 (1시간)
    }
}