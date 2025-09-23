package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pix.entity.MyIdolEntity;


@Repository
public interface MyIdolRepository extends JpaRepository<MyIdolEntity, Long> {

}
