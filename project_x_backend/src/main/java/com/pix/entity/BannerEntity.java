package com.pix.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="banners")
public class BannerEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="banner_id")
	private Long bannerIdd;
	
	@Column(name="banner_title")
	private String bannerTitle;
	
	@Column(name="start_date")
	private LocalDateTime startDate;

	@Column(name="end_date")
	private LocalDateTime endDate;
	
	@Column(name="banner_img_Url")
	private String bannerImgUrl;

}
