package com.pix.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pix.dto.Post;
import com.pix.dto.PostResponseDto;
import com.pix.dto.PostViewDto;
import com.pix.dto.User;
import com.pix.repository.PicturePostRepository;
import com.pix.repository.PictureUserRepository;
import com.pix.service.PicturePostService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/posts")
public class PicturePostController {
	@Autowired
	private PicturePostRepository postRepository;
	
	@Autowired
	private PictureUserRepository userRepository;
	
	private final PicturePostService postService;
	public PicturePostController(PicturePostService postService) {
		this.postService = postService;
	}
	
	// 전체 게시글 가져오기
	@GetMapping
	public List<PostResponseDto> getAllPosts(){
		return postRepository.findAllPostsWithUsername();
	}
	
	// 1개 게시글 가져오기
	@GetMapping("/{id}")
	public PostViewDto getPostView(@PathVariable("id") Long id) {
		PostViewDto dto = postRepository.findPostViewDtoById(id);
	    if (dto == null) {
	        throw new RuntimeException("게시글을 찾을 수 없습니다.");
	    }
	    return dto;
	}
	
	// 조회수
	@PatchMapping("/hit/{id}")
	public void incrementHit(@PathVariable("id") Long id){
		postService.incrementHit(id);
	}
	
	// 게시글 작성
	@PostMapping()
	public Post createPost(@RequestParam("title") String title,
						   @RequestParam("member") String member,
						   @RequestParam(value="content", required=false) String content,
						   @RequestParam("image") MultipartFile imageFile) {
		// 	업로드 폴더 경로 지정
		String uploadDir = "/Users/ryujaeeun/Downloads/upload/";
		
		// 파일 저장
		String fileName = System.currentTimeMillis()+"-"+imageFile.getOriginalFilename();
		File dest = new File(uploadDir + fileName);
		try {
			imageFile.transferTo(dest);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("게시글 등록 실패", e);
		}
		
		// Post 객체 생성 후 DB 저장
		Post post = new Post();
		post.setTitle(title);
		post.setMember(member);
		post.setContent(content);
		post.setImage_url("/upload/" + fileName);
		post.setHit((long) 0);
		post.setCreated_at(LocalDateTime.now());
		
		User user = userRepository.findById(1234L)
	             .orElseThrow(() -> new RuntimeException("User not found"));
		post.setUser(user);

		
		return postRepository.save(post);
	}
}
