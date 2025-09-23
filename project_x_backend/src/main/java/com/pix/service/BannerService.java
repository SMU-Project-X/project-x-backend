package com.pix.service;

import java.util.List;
import java.util.Optional;

import com.pix.entity.BannerEntity;

public interface BannerService {

	List<BannerEntity> findAll();

	BannerEntity getBannerById(Long bannerId);


}
