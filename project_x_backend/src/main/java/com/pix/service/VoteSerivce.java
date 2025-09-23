package com.pix.service;

import com.pix.entity.VoteRecord;

public interface VoteSerivce {

	VoteRecord saveVote(Long bannerId, Long optionId, String userId);

}
