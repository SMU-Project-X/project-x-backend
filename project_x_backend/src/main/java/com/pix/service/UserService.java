package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.dto.User;

@Service
public interface UserService {
	User save(User u) ;

	User findByUserIdAndPassword(String userId, String password);

	User findByNameAndEmail(String name, String email);

}
