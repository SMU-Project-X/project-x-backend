package com.pix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.MyIdolMemberEntity;
import com.pix.repository.MyIdolMemberRepository;

@Service
public class MyIdolMemberServiceImpl implements MyIdolMemberService {
	
	@Autowired MyIdolMemberRepository myIdolMemberRepository;

	@Override
	public MyIdolMemberEntity save(MyIdolMemberEntity idolMember) {
		MyIdolMemberEntity myIdolMember = myIdolMemberRepository.save(idolMember);
		return myIdolMember;
	}

}
