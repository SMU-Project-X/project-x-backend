package com.pix.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

// 게시판 목록 조회용 DTO
public class PostResponseDto {
	private Long id;
	private String title;
	private String content;
	private String member;
	private String image_url;
	private LocalDateTime created_at;
	private Long hit;
	private String username;	// users 테이블의 username
	
	// 생성자
	public PostResponseDto(Long id, String title, String content, String member, String image_url, LocalDateTime created_at, Long hit, String username) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.member = member;
		this.image_url = image_url;
		this.created_at = created_at;
		this.hit = hit;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public Long getHit() {
		return hit;
	}

	public void setHit(Long hit) {
		this.hit = hit;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
