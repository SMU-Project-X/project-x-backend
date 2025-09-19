package com.pix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.dto.MyIdol;

public interface PictureMyIdolRepository extends JpaRepository<MyIdol, Long> {
	Optional<MyIdol> findByUserId(Long userId);
}
