package com.pix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.MyIdolEntity;
import com.pix.repository.MyIdolRepository;

@Service
public class MyIdolServiceImpl implements MyIdolService {
	@Autowired MyIdolRepository myIdolRepository;

	@Override
	public MyIdolEntity save(MyIdolEntity myIdol) {
		MyIdolEntity mIdol = myIdolRepository.save(myIdol);
		return mIdol;
	}

}
