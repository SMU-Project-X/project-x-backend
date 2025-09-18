package com.pix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.UserEntity;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String>{

	Optional<UserEntity> findById(String userId);
	
}
