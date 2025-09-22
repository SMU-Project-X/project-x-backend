package com.pix.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pix.dto.MemberInfo;
import com.pix.repository.PictureMemberInfoRepository;

@RestController
@RequestMapping("/api/memberinfo")
@CrossOrigin(origins = "http://localhost:5173")
public class PictureMemberInfoController {

	private final PictureMemberInfoRepository memberRepository;

    public PictureMemberInfoController(PictureMemberInfoRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public List<MemberInfo> getAllMembers() {
        return memberRepository.findAll();
    }
}
