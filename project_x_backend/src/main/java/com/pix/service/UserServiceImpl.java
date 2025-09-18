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
		// TODO Auto-generated method stub
		return null;
	}
	

}

