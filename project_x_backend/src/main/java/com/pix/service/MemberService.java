package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.entity.MemberEntity;

@Service
public interface MemberService {

	MemberEntity findByMemberName(String memberName);

}
