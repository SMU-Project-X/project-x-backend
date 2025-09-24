package com.pix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users") // Oracle 예약어 + 대소문자 구분 주의
public class UsersEntity {
	@Id
	@Column(name = "user_id", nullable = false, length = 50)
	private String userId;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "nickname", length = 100)
	private String nickname;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "age", precision = 3)
	private Integer age;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "is_admin", length = 1)
	private String isAdmin;

	@Column(name = "created_at")
	private java.time.LocalDateTime createdAt;

	@Column(name = "updated_at")
	private java.time.LocalDateTime updatedAt;
}