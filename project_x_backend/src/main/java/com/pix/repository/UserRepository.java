package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.dto.User;

public interface UserRepository extends JpaRepository<User, String>{

	User findByUserId(String userId);

	User findByNameAndEmail(String name, String email);
}
