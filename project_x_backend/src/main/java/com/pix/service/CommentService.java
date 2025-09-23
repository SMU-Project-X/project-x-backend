package com.pix.service;

import java.util.List;

import com.pix.entity.CommentEntity;


public interface CommentService {

	// 멤버별 댓글 리스트
	List<CommentEntity> findByMember_MemberIdAndMember_MemberName(Long memberId, String memberName);

	// 댓글 저장
	CommentEntity saveComment(String content, Long memberId, String memberName);








}
