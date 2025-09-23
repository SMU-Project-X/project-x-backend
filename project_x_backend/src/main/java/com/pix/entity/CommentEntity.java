package com.pix.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@Data
@Table(name = "comment_reply")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    // FK to member_info
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MemberEntity member;

    // FK to UsersEntity (user_id: String)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsersEntity user;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    // 작성 당시 닉네임 (익명 가능)
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.isAnonymous == null) this.isAnonymous = "0";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
