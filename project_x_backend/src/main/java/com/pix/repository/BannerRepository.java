package com.pix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.BannerEntity;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long>{

}
