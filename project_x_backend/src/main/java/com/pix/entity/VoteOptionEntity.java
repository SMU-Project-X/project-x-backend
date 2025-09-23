package com.pix.entity;

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
@Table(name="VoteOption")
public class VoteOptionEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="option_id")
	private Long optionId;
	
	@Column(name="banner_id")
	private Long bannerId;
	
	@Column(name="vote_type_id")
	private Long voteTypeId;
	
	@Column(name="option_name")
	private String optionName;
	
	@Column(name="option_img_url")
	private String optionImgUrl;
	
}
