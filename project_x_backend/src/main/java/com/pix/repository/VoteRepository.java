package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.VoteRecord;

public interface VoteRepository extends JpaRepository<VoteRecord, Long>{

}
