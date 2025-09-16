package com.pix.light_stick.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "lightstick_share",
        uniqueConstraints = @UniqueConstraint(name = "uq_ls_share_code", columnNames = "code")
)
/**
 * 수동 DDL의 CHECK/JSON 제약을 하나의 CHECK로 합쳐서 부여
 * (Hibernate는 @Check를 엔티티에 1개만 다는 걸 권장 → 조건을 AND로 묶어 한 번에 기술)
 */
@Check(constraints =
        "cap_shape IN ('sphere','star','heart','hemisphere') AND " +
                "thickness IN ('thin','wide') AND " +
                "body_length IN ('short','long') AND " +
                "REGEXP_LIKE(body_color, '^#[0-9A-Fa-f]{6}$') AND " +
                "REGEXP_LIKE(cap_color, '^#[0-9A-Fa-f]{6}$') AND " +
                "metallic BETWEEN 0 AND 1 AND " +
                "roughness BETWEEN 0 AND 1 AND " +
                "transmission BETWEEN 0 AND 1 AND " +
                "sticker_scale BETWEEN 0.1 AND 1 AND " +
                "sticker_y BETWEEN 0 AND 1 AND " +
                "is_public IN (0,1) AND " +
                "spec IS JSON"
)
@Getter @Setter
public class LightstickShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle 12c+ IDENTITY 지원
    @Column(name = "share_id", nullable = false, precision = 19)
    private Long shareId;

    // FK를 자동 생성하려면 아래를 ManyToOne으로 바꿔야 함(아래 3번 참고). 지금은 컬럼만 생성.
    @Column(name = "user_id", nullable = false, precision = 19)
    private Long userId;

    @Column(name = "code", nullable = false, length = 16)
    private String code;

    @Column(name = "cap_shape", nullable = false, length = 20)
    private String capShape;

    @Column(name = "thickness", nullable = false, length = 10)
    private String thickness;

    @Column(name = "body_length", nullable = false, length = 10)
    private String bodyLength;

    @Column(name = "body_color", nullable = false, length = 7)
    private String bodyColor;

    @Column(name = "cap_color", nullable = false, length = 7)
    private String capColor;

    @Column(name = "metallic", precision = 4, scale = 3)
    private BigDecimal metallic;

    @Column(name = "roughness", precision = 4, scale = 3)
    private BigDecimal roughness;

    @Column(name = "transmission", precision = 4, scale = 3)
    private BigDecimal transmission;

    @Column(name = "figure_code", length = 50)
    private String figureCode;

    @Column(name = "sticker_scale", precision = 4, scale = 3)
    private BigDecimal stickerScale;

    @Column(name = "sticker_y", precision = 4, scale = 3)
    private BigDecimal stickerY;

    @Column(name = "sticker_asset_url", length = 512)
    private String stickerAssetUrl;

    @Lob
    @Column(name = "spec", nullable = false, columnDefinition = "CLOB")
    private String spec;

    @Column(name = "is_public", precision = 1)
    @ColumnDefault("1") // DDL에 DEFAULT 1 생성
    private Integer isPublic;

    @Column(name = "spec_hash", length = 64)
    private String specHash;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // @PrePersist / @PreUpdate 는 DB DEFAULT/UpdateTimestamp와 중복되니 제거
}
