package com.pix.service;

import static java.util.Map.of;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pix.entity.psychoTypeEntity;
import com.pix.repository.PsychoTypeRepository;

@Service
public class PsychoTypeServiceImpl implements PsychoTypeService {
	@Autowired PsychoTypeRepository psychoTypeRepository;
	
	// traits 배열을 combo_id로 변환
    public String toComboId(List<String> traits) {
    	Map<String, String> mapping = Map.ofEntries(
    		    Map.entry("귀여움", "cute"),
    		    Map.entry("도도함", "dodo"),
    		    Map.entry("리더십", "leadership"),
    		    Map.entry("상냥함", "kind"),
    		    Map.entry("지성미", "intellect"),
    		    Map.entry("사랑스러움", "lovely"),
    		    Map.entry("신비로움", "mystique"),
    		    Map.entry("장난기", "playful"),
    		    Map.entry("열정", "passion"),
    		    Map.entry("시크함", "chic"),
    		    Map.entry("차분함", "calm"),
    		    Map.entry("카리스마", "charisma")
    		);
    	
        // ② 변환 후 정렬해서 combo_id 생성
        return traits.stream()
                     .map(mapping::get)   // trait → 영문 id 매핑
                     .filter(Objects::nonNull) // 매핑 안 되는 값 제외
                     .sorted()            // 정렬 (순서 보장)
                     .collect(Collectors.joining("+"));
    }

	@Override
	// combo_id로 psychoTypeEntity 조회
    public psychoTypeEntity findByTraits(List<String> traits) {
        String psychoTypeName = toComboId(traits);
        psychoTypeEntity psychoType = psychoTypeRepository.findByPsychoTypeName(psychoTypeName);
        
        return psychoType;
                
    }

}
