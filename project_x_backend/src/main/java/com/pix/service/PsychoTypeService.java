package com.pix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pix.entity.psychoTypeEntity;

@Service
public interface PsychoTypeService {

	psychoTypeEntity findByTraits(List<String> traits);

}
