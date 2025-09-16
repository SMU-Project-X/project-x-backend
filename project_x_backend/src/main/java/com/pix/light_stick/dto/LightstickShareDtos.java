package com.pix.light_stick.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LightstickShareDtos {

    public record ShareRequest(
            @NotNull Integer schemaVersion,
            @NotNull Long clientTs,

            // enum 대신 String + 정규식
            @Pattern(regexp = "^(sphere|star|heart|hemisphere)$") @NotBlank String capShape,
            @Pattern(regexp = "^(thin|wide)$")                    @NotBlank String thickness,
            @Pattern(regexp = "^(short|long)$")                   @NotBlank String bodyLength,

            @Pattern(regexp = "^#[0-9A-Fa-f]{6}$") @NotBlank String bodyColor,
            @Pattern(regexp = "^#[0-9A-Fa-f]{6}$") @NotBlank String capColor,

            // 0~1 구간, 소수 3자리(NUMBER(4,3)) 기준
            @NotNull @DecimalMin("0.000") @DecimalMax("1.000") @Digits(integer = 1, fraction = 3) BigDecimal metallic,
            @NotNull @DecimalMin("0.000") @DecimalMax("1.000") @Digits(integer = 1, fraction = 3) BigDecimal roughness,
            @NotNull @DecimalMin("0.000") @DecimalMax("1.000") @Digits(integer = 1, fraction = 3) BigDecimal transmission,

            String figureCode, // 선택

            // 스케일: 0.100 ~ 1.000
            @NotNull @DecimalMin("0.100") @DecimalMax("1.000") @Digits(integer = 1, fraction = 3) BigDecimal stickerScale,
            @NotNull @DecimalMin("0.000") @DecimalMax("1.000") @Digits(integer = 1, fraction = 3) BigDecimal stickerY,

            String stickerAssetUrl // 선택(업로드 붙이면 사용)
    ) {}

    public record ShareResponse(Long shareId, String code) {}
}
