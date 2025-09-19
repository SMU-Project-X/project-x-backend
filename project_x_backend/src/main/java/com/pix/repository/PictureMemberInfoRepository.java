package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.dto.MemberInfo;

public interface PictureMemberInfoRepository extends JpaRepository<MemberInfo, Long> {

}
