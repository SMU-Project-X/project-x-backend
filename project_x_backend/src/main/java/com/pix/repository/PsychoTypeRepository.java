package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.psychoTypeEntity;

public interface PsychoTypeRepository extends JpaRepository<psychoTypeEntity, String> {

	psychoTypeEntity findByPsychoTypeName(String psychoTypeName);

}
