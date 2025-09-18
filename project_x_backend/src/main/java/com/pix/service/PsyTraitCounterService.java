package com.pix.service;

import com.pix.domain.PsyTraitCounter;
import com.pix.repository.PsyTraitCounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PsyTraitCounterService {

    private final PsyTraitCounterRepository repo;

    public PsyTraitCounterService(PsyTraitCounterRepository repo) {
        this.repo = repo;
    }

    /**
     * 주어진 성향(trait)을 +1 증가시키고, 증가된 총합을 반환
     * - MERGE로 원자적 증가
     * - 바로 findById로 조회하여 현재 값 리턴
     */
    @Transactional
    public long hitAndGet(String traitRaw) {
        String trait = traitRaw == null ? "" : traitRaw.trim();
        if (trait.isEmpty()) throw new IllegalArgumentException("trait is blank");
        repo.upsertInc(trait);
        return repo.findById(trait).map(PsyTraitCounter::getCnt).orElse(1L);
    }
}
