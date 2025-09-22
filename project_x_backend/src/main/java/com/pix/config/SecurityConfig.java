package com.pix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.cors()
        	.and()

            .csrf(csrf -> csrf.disable())
            
            // 🔧 요청 권한 설정 - 매우 관대하게 수정
            .authorizeHttpRequests(auth -> auth
                // 회원가입/로그인 API
                .requestMatchers("/api/signup/**").permitAll()
                .requestMatchers("/api/login/**").permitAll()

                .requestMatchers("/api/memberinfo/**").permitAll()
                .requestMatchers("/api/posts/**").permitAll()
                .requestMatchers("/upload/**").permitAll()    // 업로드 이미지 접근 허용
                .anyRequest().authenticated()
                                   
                // 🔧 상품 관련 API 모두 허용
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/api/products/search/**").permitAll()
                .requestMatchers("/api/products/*/stock").permitAll()
                
                // 🔧 기타 API들도 허용
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                
                // 🔧 정적 리소스 허용
                .requestMatchers("/files/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                
                // 🔧 에러 페이지 허용
                .requestMatchers("/error/**").permitAll()
                
                // 🔧 헬스체크 허용
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/health/**").permitAll()
                
                // 🔧 루트 경로 허용
                .requestMatchers("/", "/index.html").permitAll()
                
                // 🔧 개발 환경을 위해 나머지 요청도 허용
                .anyRequest().permitAll()  // 원래는 authenticated()였지만 개발용으로 permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}