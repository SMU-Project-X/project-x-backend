package com.pix.light_stick.controller;

import com.pix.light_stick.dto.LightstickShareDtos;
import com.pix.light_stick.service.LightstickShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/lightstick/shares
 * - 요청 JSON: ShareRequest
 * - 응답 JSON: { shareId, code }
 *
 * userId는 현재 로그인 세션/토큰 연동 전이므로,
 * 개발 편의로 요청 헤더 X-USER-ID를 임시 허용(운영 전 보안 흐름으로 교체)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lightstick")
public class LightstickShareController {

    private final LightstickShareService service;

    @PostMapping("/shares")
    public LightstickShareDtos.ShareResponse create(
            @RequestBody @Valid LightstickShareDtos.ShareRequest req,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId // 임시
    ) {
        // ⚠️ 개발 중 임시: 헤더 없으면 1L로 강제
        // 반드시 users(user_id=1) 더미 계정 하나 만들어 두세요(외래키 제약 때문)
        if (userId == null) userId = 1L;

        return service.createShare(userId, req);
    }
}
