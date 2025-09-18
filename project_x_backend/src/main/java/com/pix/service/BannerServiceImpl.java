package com.pix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.BannerEntity;
import com.pix.repository.BannerRepository;

@Service
public class BannerServiceImpl implements BannerService{

	@Autowired BannerRepository bannerRepository;	
	
	@Override
	public List<BannerEntity> findAll() {
		List<BannerEntity> banners = bannerRepository.findAll();
		return banners;
	}

}
