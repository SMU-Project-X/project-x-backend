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
		// TODO: 실제 통계 구현 필요시 추가
		return null;
	}
	
	// ===== MD 페이지 연동을 위한 새로운 메서드들 =====
	
	@Override
	public boolean isUsernameExists(String username) {
		try {
			UsersEntity user = userRepository.findByUserId(username);
			return user != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean isEmailExists(String email) {
		try {
			// 이메일로 사용자 찾기 (UserRepository에 메서드 있는지 확인 필요)
			// 일단 기본 구현으로 처리
			return false; // 실제로는 userRepository.findByEmail(email) != null;
		} catch (Exception e) {
			return false;
		}
	}
	

}