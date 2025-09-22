package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.entity.MyIdolMemberEntity;

@Service
public interface MyIdolMemberService {
	MyIdolMemberEntity save(MyIdolMemberEntity idolMember);

}
