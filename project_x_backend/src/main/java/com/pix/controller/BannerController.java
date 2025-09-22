package com.pix.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pix.entity.BannerEntity;
import com.pix.service.BannerService;

@RestController
@RequestMapping("api/banners")
public class BannerController {
	
	@Autowired BannerService bannerService;
	
    // 배너 하나 가져오기
    @GetMapping("/{bannerId}")
    public BannerEntity getBanner(@PathVariable Long bannerId) {
    	BannerEntity getBanner = bannerService.getBannerById(bannerId);
        return getBanner;
    }
	
	
	// 배너 전체 리스트 
	@GetMapping("/list")
	public List<BannerEntity> getBanners() {
		List<BannerEntity> banners = bannerService.findAll();
		return banners;
	}

}
