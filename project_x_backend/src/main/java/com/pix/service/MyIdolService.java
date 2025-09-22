package com.pix.service;

import org.springframework.stereotype.Service;

import com.pix.entity.MyIdolEntity;

@Service
public interface MyIdolService {

	MyIdolEntity save(MyIdolEntity myIdol);

}
