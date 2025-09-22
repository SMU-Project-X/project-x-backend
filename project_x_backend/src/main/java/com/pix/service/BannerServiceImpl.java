package com.pix.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.BannerEntity;
import com.pix.repository.BannerRepository;

@Service
public class BannerServiceImpl implements BannerService{

	@Autowired BannerRepository bannerRepository;	
	
	// 전체 리스트
	@Override
	public List<BannerEntity> findAll() {
		List<BannerEntity> banners = bannerRepository.findAll();
		return banners;
	}

	// 배너 하나 가져오기
	@Override
	public BannerEntity getBannerById(Long bannerId) {
		BannerEntity getBanner = bannerRepository.findById(bannerId)
				.orElseThrow(()->new RuntimeException("배너를 찾을 수 없습니다."+bannerId));
		return getBanner;
	}


}
