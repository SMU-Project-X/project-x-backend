package com.pix.service;

import com.pix.entity.UsersEntity;
import com.pix.dto.UserDto.UserStats;

public interface UserService {
    
    // ===== 기존 메서드들 (그대로 유지) =====
    
    /**
     * 사용자 저장 (회원가입)
     */
    UsersEntity save(UsersEntity u);
    
    /**
     * 아이디와 비밀번호로 사용자 찾기 (로그인)
     */
    UsersEntity findByUserIdAndPassword(String userId, String password);
    
    /**
     * 이름과 이메일로 사용자 찾기 (아이디 찾기)
     */
    UsersEntity findByNameAndEmail(String name, String email);
    
    /**
     * 사용자 통계 조회
     */
    UserStats getUserStats();
    
    // ===== MD 페이지 헤더 연동을 위한 새로운 메서드들 =====
    
    /**
     * 사용자명 중복 체크
     */
    boolean isUsernameExists(String username);
    
    /**
     * 이메일 중복 체크  
     */
    boolean isEmailExists(String email);
}