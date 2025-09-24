package com.pix.controller;

import com.pix.entity.MemberEntity;
//import com.pix.service.MemberService;
import com.pix.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*") // React 포트 허용
public class MemberController {
	
	@Autowired MemberRepository memberRepository;
	
	@Autowired HttpSession session;

	
	// 멤버 불러오기
	@GetMapping("/select")
	public List<MemberEntity> member() {
		// 기존 세션 키로 확인
		String sessionUserId = (String) session.getAttribute("session_id");
		String sessionName = (String) session.getAttribute("session_name");
		
		System.out.println("UserId: " + sessionUserId);
		System.out.println("Name: " + sessionName);
		
		List<MemberEntity> member = memberRepository.findAll();
		return member;
	}
	
	
}
