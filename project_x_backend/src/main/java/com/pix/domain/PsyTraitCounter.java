package com.pix.domain;

import jakarta.persistence.*;

/**
 * 성향별 카운터 테이블
 * - PK: TRAIT (문자)
 * - CNT: 누적 수치
 * JPA가 ddl-auto=create|update 일 때 테이블 자동 생성
 */
@Entity
@Table(name = "PSY_TRAIT_COUNTER")
public class PsyTraitCounter {

    @Id
    @Column(name = "TRAIT", length = 50, nullable = false)
    private String trait;

    @Column(name = "CNT", nullable = false)
    private Long cnt = 0L;

    protected PsyTraitCounter() {} // JPA 기본 생성자

    public PsyTraitCounter(String trait, Long cnt) {
        this.trait = trait;
        this.cnt = cnt;
    }

    public String getTrait() { return trait; }
    public Long getCnt() { return cnt; }
    public void setCnt(Long cnt) { this.cnt = cnt; }
}
