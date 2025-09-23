package com.pix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.MyIdolMemberInfo;

@Repository
public interface MyIdolMemberInfoRepository extends JpaRepository<MyIdolMemberInfo, Long> {

	List<MyIdolMemberInfo> findAll();
}
