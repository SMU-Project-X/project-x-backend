package com.pix.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	
	// 멤버멸 댓글 가져오기
	List<CommentEntity> findByMember_MemberIdAndMember_MemberName(Long memberId, String memberName);

}
