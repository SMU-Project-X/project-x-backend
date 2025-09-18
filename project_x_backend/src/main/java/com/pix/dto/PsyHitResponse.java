package com.pix.dto;

/**
 * 증가 처리 후 현재 총합을 반환하기 위한 응답 바디
 * 예) { "trait": "신비로움", "total": 42 }
 */
public class PsyHitResponse {
    private final String trait;
    private final long total;

    public PsyHitResponse(String trait, long total) {
        this.trait = trait;
        this.total = total;
    }

    public String getTrait() { return trait; }
    public long getTotal() { return total; }
}
