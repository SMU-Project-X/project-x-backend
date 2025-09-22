package com.pix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pix.entity.VoteOptionEntity;
import com.pix.entity.VoteRecord;
import com.pix.repository.VoteRepository;
import com.pix.service.VoteSerivce;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
	
	@Autowired VoteSerivce voteService;
	
    @PostMapping("/save")
    public VoteRecord saveVote(@RequestBody VoteRecord request) {
        return voteService.saveVote(
            request.getBannerId(),
            request.getOptionId(),
            request.getUserId()
        );
    }

}
