package com.pix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pix.entity.MemberEntity;

@Service
public interface MemberService {

	MemberEntity findByMemberName(String memberName);

	List<MemberEntity> findAll();

}
