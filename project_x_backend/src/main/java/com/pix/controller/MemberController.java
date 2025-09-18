package com.pix.controller;

import com.pix.entity.MemberEntity;
//import com.pix.service.MemberService;
import com.pix.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*") // React 포트 허용
public class MemberController {
	
	@Autowired MemberRepository memberRepository;

	
	// 멤버 불러오기
	@GetMapping("/select")
	public List<MemberEntity> member() {
		List<MemberEntity> member = memberRepository.findAll();
		return member;
	}


}
