package com.pix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pix.entity.VoteOptionEntity;
import com.pix.entity.VoteRecord;
import com.pix.repository.VoteRepository;
import com.pix.service.VoteSerivce;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
	
	@Autowired VoteSerivce voteService;
	
//	// 투표 결과 보여주기
//	@GetMapping("/voteResult/{bannerId}")
//	public ResponseEntity<VoteRecord> voteResult(
//			
//			return voteResult;
//	}
//	
	
	// 투표 저장하기
    @PostMapping("/save")
    public VoteRecord saveVote(@RequestBody VoteRecord request) {
        return voteService.saveVote(
            request.getBannerId(),
            request.getOptionId(),
            request.getUserId()
        );
    }

}
