package com.pix.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 프론트에서 전달하는 1순위 성향 하나만 담는 요청 바디
 * 예) { "trait": "신비로움" }
 */
public class PsyHitRequest {
    @NotBlank
    private String trait;

    public String getTrait() { return trait; }
    public void setTrait(String trait) { this.trait = trait; }
}
