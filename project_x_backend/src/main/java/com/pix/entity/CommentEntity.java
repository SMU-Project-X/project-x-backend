package com.pix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
@Table(name = "comment_reply")
public class CommentEntity {
	
	// 답글 달기: 댓글 시퀀스번호, 아이돌 이름, 아이디, 유저 아이디, 유저 닉네임, 유저 이미지, 유저의 댓글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    // FK to member_info
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MemberEntity member;

    // USER 테이블과 FK (로그인 유저)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserEntity user;
    
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    // 유저닉네임
    @Column(length = 100)
    private String nickname;

    @Column(name = "display_avatar_url", length = 255)
    private String displayAvatarUrl;

    @Column(name = "is_anonymous", nullable = false, length = 1)
    private String isAnonymous;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

	
}
