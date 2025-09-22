package com.pix.controller;

import com.pix.entity.MyIdolMemberInfo;
//import com.pix.service.MemberService;
import com.pix.repository.MyIdolMemberInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	
	@Autowired MyIdolMemberInfoRepository memberRepository;

	
	// 멤버 불러오기
	@GetMapping("/select")
	public List<MyIdolMemberInfo> member() {
		List<MyIdolMemberInfo> member = memberRepository.findAll();
		return member;
	}


}
