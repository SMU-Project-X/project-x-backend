package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.dto.UserDto;
import com.pix.dto.UserDto.UserStats;
import com.pix.entity.UsersEntity;


@Service
public interface UserService {
	UsersEntity save(UsersEntity u) ;

	UsersEntity findByUserIdAndPassword(String userId, String password);

	UsersEntity findByNameAndEmail(String name, String email);

	UserStats getUserStats();

}

