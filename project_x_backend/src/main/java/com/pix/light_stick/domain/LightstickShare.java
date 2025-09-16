package com.pix.light_stick.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lightstick_share",
        uniqueConstraints = @UniqueConstraint(name = "uq_ls_share_code", columnNames = "code"))
@Getter @Setter
public class LightstickShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_id", nullable = false, precision = 19)
    private Long shareId;

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
    private Integer isPublic;

    @Column(name = "spec_hash", length = 64)
    private String specHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        if (isPublic == null) isPublic = 1;
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }
}
