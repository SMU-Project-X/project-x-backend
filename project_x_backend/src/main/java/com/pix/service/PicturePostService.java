package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.dto.Post;
import com.pix.repository.PicturePostRepository;

@Service
public class PicturePostService {
	private final PicturePostRepository postRepository;
	
	// 생성자에서 초기화
	public PicturePostService(PicturePostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	// 조회수 증가 -> 저장 -> 최신 객체 반환
	public Post incrementHit(Long id) {
		return postRepository.findById(id).map(post -> {
			post.setHit(post.getHit() +1);
			return postRepository.save(post);
		}).orElseThrow(() -> new RuntimeException("Post not found"));
	}
	
	public Post getPost(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Post not found"));
	}
}
