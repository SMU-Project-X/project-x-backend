package com.pix.controller;

import java.util.List;
import java.util.stream.Collectors;

//import org.hibernate.mapping.Map;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pix.dto.CommentDto;
//import com.pix.dto.CommentDto;
import com.pix.entity.CommentEntity;
import com.pix.repository.CommentRepository;
import com.pix.repository.MyIdolMemberInfoRepository;
import com.pix.service.CommentService;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private static final com.pix.dto.CommentDto CommentDto = null;
	@Autowired MyIdolMemberInfoRepository memberRepository;
	@Autowired CommentRepository commentRepository;
	@Autowired CommentService commentService;
	
	// 생성자 주입(불피요한 의존성 제거)
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	
	@PostMapping("/save")
	public ResponseEntity<CommentEntity> saveComment(
//			@RequestParam("memberId") Long memberId,@RequestParam("name") String name,
			@RequestBody CommentDto commentDto){
		
//		ResponseEntity<CommentEntity> saveComment = commentService.saveComment(commentEntity, memberId,name);
		CommentEntity saved = commentService.saveComment(
				commentDto.getContent(), commentDto.getMemberId(), commentDto.getMemberName()
				);

		System.out.println("받은 DTO: " +commentDto);
	    return ResponseEntity.ok(saved);
	}
		
	
	// 멤버별 댓글 리스트
	@GetMapping("/search")
	public List<CommentEntity> getComments(
	        @RequestParam("memberId") Long memberId,	
	        @RequestParam("memberName") String memberName) {	
	    List<CommentEntity> comment = commentService.findByMember_MemberIdAndMember_MemberName(memberId, memberName);
	    
	    return comment;
	}
	
}