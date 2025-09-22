package com.pix.dto;

import java.time.LocalDateTime;

import com.pix.entity.CommentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private Long memberId;
    private String memberName;
    private String content;
    private String userId;
    private String nickname;
    private String displayAvatarUrl;
    private String isAnonymous;
    private String ipAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(CommentEntity entity) {
        this.commentId = entity.getCommentId();

        if (entity.getMember() != null) {
            this.memberId = entity.getMember().getMemberId();
            this.memberName = entity.getMember().getMemberName();
        }

        this.content = entity.getContent();

        if (entity.getUser() != null) {
            this.userId = entity.getUser().getUserId();
            this.nickname = entity.getUser().getNickname();
        }

        this.displayAvatarUrl = entity.getDisplayAvatarUrl();
        this.isAnonymous = entity.getIsAnonymous();
        this.ipAddress = entity.getIpAddress();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
