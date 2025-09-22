package com.pix.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pix.dto.MemberInfo;
import com.pix.repository.PictureMemberInfoRepository;

@RestController
@RequestMapping("/api/memberName")
@CrossOrigin(origins = "http://localhost:5173")
public class PictureMemberInfoController {
	private final PictureMemberInfoRepository memberInfoRepository;
	
	public PictureMemberInfoController(PictureMemberInfoRepository memberInfoRepository) {
		this.memberInfoRepository = memberInfoRepository;
	}
	
	@GetMapping
	public List<MemberInfo> getAllMembers(){
		return memberInfoRepository.findAll();
	}
}
