package com.pix.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pix.dto.MyIdol;
import com.pix.dto.MyIdolMemberDto;
import com.pix.repository.PictureMyIdolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PictureMyIdolService {
	private final PictureMyIdolRepository pictureMyIdolRepository;
	
	public PictureMyIdolService(PictureMyIdolRepository pictureMyIdolRepository) {
		this.pictureMyIdolRepository = pictureMyIdolRepository;
	}
	
	public List<MyIdolMemberDto> getMyIdolMembers(Long userId) {
		MyIdol myIdol = pictureMyIdolRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("마이아이돌을 찾을 수 없습니다."));
		
		return myIdol.getMembers().stream()
				.map(member -> new MyIdolMemberDto(member.getMemberInfo().getName(), 
						member.getMemberInfo().getProfileImageUrl(), 
						member.getPosition()))
				.collect(Collectors.toList());
	}
}
