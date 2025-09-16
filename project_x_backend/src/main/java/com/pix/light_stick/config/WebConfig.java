package com.pix.light_stick.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 1) CORS: Vite(5173)에서 오는 프론트 요청 허용
 * 2) 정적 리소스 매핑: /files/** → 파일시스템(app.upload-dir) 경로 노출
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.yml에 설정한 업로드 루트 디렉토리 경로
    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173") // 프론트 개발 서버
                .allowedMethods("GET","POST","PUT","DELETE","PATCH")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 예) app.upload-dir=./uploads/stickers → 절대경로로 변환 후 /files/** 에 매핑
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString() + "/";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + absolutePath);
    }
}
