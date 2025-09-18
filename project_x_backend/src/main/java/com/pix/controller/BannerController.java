package com.pix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pix.entity.BannerEntity;
import com.pix.service.BannerService;

@RestController
@RequestMapping("api/banners")
@CrossOrigin(origins="*")
public class BannerController {
	
	@Autowired BannerService bannerService;
	
	// 배너 전체 리스트 
	@GetMapping("/list")
	public List<BannerEntity> getBanners() {
		List<BannerEntity> banners = bannerService.findAll();
		
		return banners;
	}

}
