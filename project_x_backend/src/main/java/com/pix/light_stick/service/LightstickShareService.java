package com.pix.light_stick.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pix.light_stick.domain.LightstickShare;
import com.pix.light_stick.dto.LightstickShareDtos;
import com.pix.light_stick.repository.LightstickShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LightstickShareService {

    private final LightstickShareRepository repo;
    private final ObjectMapper om = new ObjectMapper(); // 간단 사용

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 헷갈리는 0/O/1/I 제외

    /** 공유 코드 10자리 생성 */
    private String newCode() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CODE_CHARS.charAt(rnd.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }

    /** SHA-256 해시(16진) */
    private String sha256Hex(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(d.length * 2);
            for (byte b : d) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LightstickShareDtos.ShareResponse createShare(Long userId, LightstickShareDtos.ShareRequest req) {
        // 1) 엔티티 구성 (소수 3자리 고정)
        LightstickShare e = new LightstickShare();
        e.setUserId(userId);

        e.setCapShape(req.capShape());
        e.setThickness(req.thickness());
        e.setBodyLength(req.bodyLength());

        // 소문자 HEX로 정규화(컬러 비교/해시 일관성)
        e.setBodyColor(req.bodyColor().toLowerCase());
        e.setCapColor(req.capColor().toLowerCase());

        e.setMetallic(req.metallic().setScale(3, RoundingMode.HALF_UP));
        e.setRoughness(req.roughness().setScale(3, RoundingMode.HALF_UP));
        e.setTransmission(req.transmission().setScale(3, RoundingMode.HALF_UP));

        e.setFigureCode(req.figureCode()); // null 허용

        e.setStickerScale(req.stickerScale().setScale(3, RoundingMode.HALF_UP));
        e.setStickerY(req.stickerY().setScale(3, RoundingMode.HALF_UP));
        e.setStickerAssetUrl(req.stickerAssetUrl()); // null 허용

        e.setIsPublic(1); // 기본 공개

        // 2) spec JSON 스냅샷(요청 페이로드 기반)
        try {
            Map<String, Object> spec = new LinkedHashMap<>();
            spec.put("schemaVersion", req.schemaVersion());
            spec.put("clientTs", req.clientTs());
            spec.put("capShape", req.capShape());
            spec.put("thickness", req.thickness());
            spec.put("bodyLength", req.bodyLength());
            spec.put("bodyColor", req.bodyColor().toLowerCase());
            spec.put("capColor", req.capColor().toLowerCase());
            spec.put("metallic", e.getMetallic());
            spec.put("roughness", e.getRoughness());
            spec.put("transmission", e.getTransmission());
            spec.put("figureCode", req.figureCode());
            spec.put("stickerScale", e.getStickerScale());
            spec.put("stickerY", e.getStickerY());
            spec.put("stickerAssetUrl", e.getStickerAssetUrl());

            String json = om.writeValueAsString(spec);
            e.setSpec(json);
            e.setSpecHash(sha256Hex(json));
        } catch (Exception ex) {
            // JSON 변환 실패 시 400 성격
            throw new IllegalArgumentException("잘못된 요청 형식입니다(spec 직렬화 실패).");
        }

        // 3) 공유 코드 충돌 방지 재시도
        for (int i = 0; i < 5; i++) {
            e.setCode(newCode());
            try {
                LightstickShare saved = repo.save(e);
                return new LightstickShareDtos.ShareResponse(saved.getShareId(), saved.getCode());
            } catch (DataIntegrityViolationException dup) {
                // UK(code) 충돌 시 재시도
                if (i == 4) throw new IllegalStateException("코드 생성 충돌이 지속됩니다. 잠시 후 다시 시도해주세요.");
            }
        }
        // 도달 불가
        throw new IllegalStateException("알 수 없는 오류");
    }
}
