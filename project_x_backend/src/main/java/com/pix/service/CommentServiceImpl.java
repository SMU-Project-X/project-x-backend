package com.pix.service;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.List;

import com.pix.repository.MyIdolMemberInfoRepository;
import com.pix.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.ProjectXBackendApplication;
import com.pix.dto.CommentDto;
import com.pix.entity.CommentEntity;
import com.pix.entity.MemberEntity;
import com.pix.entity.UsersEntity;
import com.pix.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    private final ProjectXBackendApplication projectxApplication;
    
    @Autowired
    private MyIdolMemberInfoRepository memberRepository;
    
    @Autowired 
    private CommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;
    

    public CommentServiceImpl(CommentRepository commentRepository, MyIdolMemberInfoRepository memberRepository, ProjectXBackendApplication projectxApplication) {
		 this.commentRepository = commentRepository;
		 this.memberRepository = memberRepository;
		 this.projectxApplication = projectxApplication;
	 }

    
	 // 멤버별 댓글리스트
	 @Override
	 public List<CommentEntity> findByMember_MemberIdAndMember_MemberName(Long memberId, String memberName) {
		
		return commentRepository.findByMember_MemberIdAndMember_MemberName(memberId, memberName);
	 }

	 
	 @Override
	 public CommentEntity saveComment(String content, Long memberId, String nickname) {
		 
		// memberId 로 MemberEntity 를 조회
		 MemberEntity member = memberRepository.findById(memberId)
	            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버 ID: " + memberId));

	     CommentEntity commentEntity = new CommentEntity();
	     commentEntity.setMember(member);
	     commentEntity.setContent(content);
	     commentEntity.setNickname("익명");
	     commentEntity.setIsAnonymous(null);
	     commentEntity.setCreatedAt(LocalDateTime.now());
	     commentEntity.setUpdatedAt(LocalDateTime.now());


	     return commentRepository.save(commentEntity);
	 }



}
