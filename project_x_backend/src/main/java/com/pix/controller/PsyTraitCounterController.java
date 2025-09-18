package com.pix.controller;

import com.pix.domain.PsyTraitCounter;
import com.pix.dto.PsyHitRequest;
import com.pix.dto.PsyHitResponse;
import com.pix.repository.PsyTraitCounterRepository;
import com.pix.service.PsyTraitCounterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * 엔드포인트
 * - POST /api/psytest/hit  : 1순위 성향 +1 (프론트 fire-and-forget 전송)
 * - GET  /api/psytest/stats: 전체 통계 (내림차순)
 */
@RestController
@RequestMapping("/api/psytest")
public class PsyTraitCounterController {

    private final PsyTraitCounterService service;
    private final PsyTraitCounterRepository repo;

    public PsyTraitCounterController(PsyTraitCounterService service,
                                     PsyTraitCounterRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/hit")
    public ResponseEntity<PsyHitResponse> hit(@Valid @RequestBody PsyHitRequest req) {
        long total = service.hitAndGet(req.getTrait());
        return ResponseEntity.ok(new PsyHitResponse(req.getTrait().trim(), total));
    }

    @GetMapping("/stats")
    @Transactional(readOnly = true)
    public ResponseEntity<List<PsyHitResponse>> stats() {
        List<PsyHitResponse> list = repo.findAll().stream()
                .sorted(Comparator.comparingLong(PsyTraitCounter::getCnt).reversed())
                .map(tc -> new PsyHitResponse(tc.getTrait(), tc.getCnt()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
