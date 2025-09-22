package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.MyIdolEntity;


public interface MyIdolRepository extends JpaRepository<MyIdolEntity, Long> {

	MyIdolEntity save(MyIdolEntity myIdol);

}
