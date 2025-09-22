package com.pix.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="VoteRecord")
public class VoteRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="vote_id")
	private Long voteId;
	
	@Column(name = "banner_id")
	private Long bannerId; 
	
	@Column(name = "option_id")
	private Long optionId; 
	
	@Column(name = "user_id")
	private String userId; 
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "banner_id", nullable = false)
//	private BannerEntity banner;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "option_id", nullable = false)
//	@Column(name="option_id")
//	private VoteOptionEntity voteOption;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id", nullable = false)
//	private UsersEntity user;
	
	@Column(name="ip_address")
	private String ipAddress;
	
	@Column(name="vote_time")
	private LocalDateTime voteTime;
	
	

}
