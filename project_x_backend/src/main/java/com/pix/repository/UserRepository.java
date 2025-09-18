package com.pix.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.UsersEntity;



public interface UserRepository extends JpaRepository<UsersEntity, String>{

	UsersEntity findByUserId(String userId);

	UsersEntity findByNameAndEmail(String name, String email);
}

