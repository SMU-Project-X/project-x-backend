package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.MyIdolMemberEntity;

public interface MyIdolMemberRepository extends JpaRepository<MyIdolMemberEntity,Long> {

}
