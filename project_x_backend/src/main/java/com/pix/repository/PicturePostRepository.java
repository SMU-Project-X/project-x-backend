package com.pix.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pix.dto.Post;
import com.pix.dto.PostResponseDto;
import com.pix.dto.PostViewDto;

public interface PicturePostRepository extends JpaRepository<Post, Long> {
	@Query("SELECT new com.pix.dto.PostResponseDto(" +
		       "p.id, p.title, p.content, p.member, p.image_url, p.created_at, p.hit, u.nickname) " +
		       "FROM Post p JOIN p.user u ORDER BY p.created_at DESC")
		List<PostResponseDto> findAllPostsWithUsername();
	
	@Query("SELECT new com.pix.dto.PostViewDto(" +
		       "p.id, p.title, p.content, p.member, p.image_url, p.created_at, p.hit, u.nickname) " +
		       "FROM Post p JOIN p.user u WHERE p.id = :id")
		PostViewDto findPostViewDtoById(@Param("id") Long id);

}
