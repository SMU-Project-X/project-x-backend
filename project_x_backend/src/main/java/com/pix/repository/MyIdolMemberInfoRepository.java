package com.pix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.MemberEntity;

@Repository
public interface MyIdolMemberInfoRepository extends JpaRepository<MemberEntity, Long> {

	List<MemberEntity> findAll();
}
