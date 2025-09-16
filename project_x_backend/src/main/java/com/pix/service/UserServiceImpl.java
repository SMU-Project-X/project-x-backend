package com.pix.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pix.dto.User;
import com.pix.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public User save(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		User user = userRepository.save(u);
		return user;
	}
	

}
