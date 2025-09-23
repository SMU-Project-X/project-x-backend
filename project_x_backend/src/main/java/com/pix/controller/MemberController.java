package com.pix.controller;

import com.pix.entity.MemberEntity;
//import com.pix.service.MemberService;
import com.pix.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	
	@Autowired MemberService memberService;
	
	
	// 멤버 불러오기
	@GetMapping("/select")
	public List<MemberEntity> member() {
		List<MemberEntity> members = memberService.findAll();
		System.out.println("멤버 전체 리스트: "+members);
		return members;
	}


}
