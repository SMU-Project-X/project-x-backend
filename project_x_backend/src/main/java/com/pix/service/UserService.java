package com.pix.service;

import com.pix.dto.UserDto;
import java.util.List;

public interface UserService {

    /**
     * 사용자 로그인
     */
    UserDto.LoginResponse login(UserDto.LoginRequest request);

    /**
     * 사용자 회원가입
     */
    UserDto.Response register(UserDto.RegisterRequest request);

    /**
     * 사용자 ID로 조회
     */
    UserDto.Response getUserById(Long userId);

    /**
     * 사용자명으로 조회
     */
    UserDto.Response getUserByUsername(String username);

    /**
     * 사용자 정보 수정
     */
    UserDto.Response updateUser(Long userId, UserDto.UpdateRequest request);

    /**
     * 사용자 삭제
     */
    void deleteUser(Long userId);

    /**
     * 사용자명 중복 체크
     */
    boolean isUsernameExists(String username);

    /**
     * 이메일 중복 체크
     */
    boolean isEmailExists(String email);

    /**
     * 전체 사용자 목록 조회 (관리자용)
     */
    List<UserDto.Response> getAllUsers();

    /**
     * 사용자 통계 정보 조회 (관리자용)
     */
    UserDto.UserStats getUserStats();

    /**
     * 내부 클래스: 사용자 통계
     */
    public static class UserStats {
        private long totalUsers;
        private long normalUsers;
        private long adminUsers;
        
        public UserStats(long totalUsers, long normalUsers, long adminUsers) {
            this.totalUsers = totalUsers;
            this.normalUsers = normalUsers;
            this.adminUsers = adminUsers;
        }
        
        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getNormalUsers() { return normalUsers; }
        public long getAdminUsers() { return adminUsers; }
    }
}