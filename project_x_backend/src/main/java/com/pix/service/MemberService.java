package com.pix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pix.entity.MemberEntity;
import com.pix.entity.MyIdolMemberInfo;

@Service
public interface MemberService {

	MemberEntity findByMemberName(String memberName);

	List<MemberEntity> findAll();

}
