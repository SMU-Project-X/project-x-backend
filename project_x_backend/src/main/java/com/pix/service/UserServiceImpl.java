package com.pix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pix.entity.UsersEntity;
import com.pix.dto.UserDto.UserStats;
import com.pix.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// ===== 기존 메서드들 (그대로 유지) =====
	
	@Override
	public UsersEntity save(UsersEntity u) {
		u.setPasswordHash(passwordEncoder.encode(u.getPasswordHash()));
		UsersEntity user = userRepository.save(u);
		return user;
	}

	@Override
	public UsersEntity findByUserIdAndPassword(String userId, String password) {
		UsersEntity user = userRepository.findByUserId(userId);
		if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
	        return user;
	    }
	    return null; // 로그인 실패
	}

	@Override
	public UsersEntity findByNameAndEmail(String name, String email) {
		UsersEntity user = userRepository.findByNameAndEmail(name, email);
		return user;
	}

	@Override
	public UserStats getUserStats() {
		try {
			// 🔥 실제 통계 구현 - 개선된 메서드 사용
			long totalUsers = userRepository.count();
			long adminUsers = userRepository.countAdminUsers(); // 커스텀 쿼리 사용
			long normalUsers = userRepository.countNormalUsers(); // 커스텀 쿼리 사용
			
			System.out.println("📊 사용자 통계: 전체=" + totalUsers + ", 일반=" + normalUsers + ", 관리자=" + adminUsers);
			
			return new UserStats(totalUsers, normalUsers, adminUsers);
		} catch (Exception e) {
			System.err.println("사용자 통계 조회 실패: " + e.getMessage());
			// 에러 발생 시 기본값 반환
			return new UserStats(0, 0, 0);
		}
	}
	
	// ===== MD 페이지 연동을 위한 새로운 메서드들 =====
	
	@Override
	public boolean isUsernameExists(String username) {
		try {
			// 🔥 개선된 사용자명 중복 체크
			return userRepository.existsByUserId(username);
		} catch (Exception e) {
			System.err.println("사용자명 중복 체크 실패: " + e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean isEmailExists(String email) {
		try {
			// 🔥 개선된 이메일 중복 체크 (UserRepository에 메서드 추가됨)
			return userRepository.existsByEmail(email);
		} catch (Exception e) {
			System.err.println("이메일 중복 체크 실패: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 🔥 사용자 ID로 사용자 찾기 (권한 체크용) - 새로 추가
	 */
	@Override
	public UsersEntity findByUserId(String userId) {
		try {
			return userRepository.findByUserId(userId);
		} catch (Exception e) {
			System.err.println("사용자 조회 실패 (ID: " + userId + "): " + e.getMessage());
			return null;
		}
	}
}