package com.pix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.MemberEntity;
import com.pix.repository.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired MemberRepository memberRepository;
	
	@Override
	public MemberEntity findByMemberName(String memberName) {
		MemberEntity member = memberRepository.findByMemberName(memberName);
		return member;
	}

}
