package com.pix.dto;

import java.time.LocalDateTime;

// 게시글 상세 조회용 DTO
public class PostViewDto {
	private Long id;
	private String title;
	private String content;
	private String member;
	private String image_url;
	private LocalDateTime created_at;
	private Long hit;
	private String nickname;
	
	// 생성자 추가
	public PostViewDto(Long id, String title, String content, String member,
            String image_url, LocalDateTime created_at, Long hit, String nickname) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.member = member;
		this.image_url = image_url;
		this.created_at = created_at;
		this.hit = hit;
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
