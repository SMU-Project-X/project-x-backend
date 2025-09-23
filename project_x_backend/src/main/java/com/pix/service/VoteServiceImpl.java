package com.pix.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.VoteRecord;
import com.pix.repository.VoteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VoteServiceImpl implements VoteSerivce{
	
	@Autowired VoteRepository voteRepository;

	@Override
	public VoteRecord saveVote(Long bannerId, Long optionId, String userId) {
		VoteRecord vote = VoteRecord.builder()
                .bannerId(bannerId)
                .optionId(optionId)
                .userId(userId)
                .voteTime(LocalDateTime.now())
                .build();
		 return voteRepository.save(vote);
	}

}
