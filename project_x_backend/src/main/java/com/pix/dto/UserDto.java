package com.pix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        @NotBlank(message = "사용자명을 입력해주세요")
        private String username;
        
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4-20자 사이여야 합니다")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterRequest {
        @NotBlank(message = "사용자명을 입력해주세요")
        @Size(min = 3, max = 50, message = "사용자명은 3-50자 사이여야 합니다")
        private String username;
        
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4-20자 사이여야 합니다")
        private String password;
        
        @Email(message = "올바른 이메일 형식을 입력해주세요")
        private String email;
        
        private String name;
        private String nickname;
        private Integer age;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long userId;
        private String username;
        private String email;
        private String name;
        private String nickname;
        private Integer age;
        private String address;
        private Boolean isAdmin;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        // 패스워드는 응답에 포함하지 않음 (보안)
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private boolean success;
        private String message;
        private Response user;
        private String token; // 추후 JWT 토큰 사용시
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String name;
        private String nickname;
        private Integer age;
        private String address;
        private String email;
    }

    /**
     * 🔥 중요: UserStats 클래스 추가 (AdminController와 UserController에서 사용)
     * 사용자 통계 정보를 담는 DTO 클래스
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserStats {
        private long totalUsers;      // 전체 사용자 수
        private long normalUsers;     // 일반 사용자 수
        private long adminUsers;      // 관리자 수
        private LocalDateTime lastUpdated; // 마지막 업데이트 시간
        
        // 생성자 오버로드 (LocalDateTime 없이도 생성 가능)
        public UserStats(long totalUsers, long normalUsers, long adminUsers) {
            this.totalUsers = totalUsers;
            this.normalUsers = normalUsers;
            this.adminUsers = adminUsers;
            this.lastUpdated = LocalDateTime.now();
        }
        
        // 비율 계산 메서드들
        public double getNormalUserPercentage() {
            return totalUsers > 0 ? (double) normalUsers / totalUsers * 100 : 0;
        }
        
        public double getAdminUserPercentage() {
            return totalUsers > 0 ? (double) adminUsers / totalUsers * 100 : 0;
        }
    }
}