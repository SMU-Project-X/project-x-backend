package com.pix.light_stick.controller;

import com.pix.light_stick.dto.LightstickShareDtos;
import com.pix.light_stick.service.LightstickShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lightstick")
public class LightstickShareController {

    private final LightstickShareService service;

    // 업로드 루트 (WebConfig가 /files/** 로 매핑)
    @Value("${app.upload-dir:./uploads/stickers}")
    private String uploadDir;

    private static final Set<String> ALLOWED = Set.of(
            "image/png", "image/jpeg", "image/webp", "image/gif"
    );

    @PostMapping("/shares")
    public LightstickShareDtos.ShareResponse create(
            @RequestBody @Valid LightstickShareDtos.ShareRequest req,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        if (userId == null) userId = 1L;
        return service.createShare(userId, req);
    }

    // 컨트롤러 내부에서 직접 파일 저장
    @PostMapping(value = "/stickers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadSticker(@RequestPart("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }
        String ctype = file.getContentType();
        if (ctype == null || !ALLOWED.contains(ctype)) {
            throw new IllegalArgumentException("허용되지 않는 이미지 형식입니다. (png/jpeg/webp/gif)");
        }
        String ext = switch (ctype) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            case "image/gif" -> "gif";
            default -> "bin";
        };

        LocalDate today = LocalDate.now();
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path folder = root.resolve(today.toString()); // e.g. 2025-09-16
        Files.createDirectories(folder);

        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path dest = folder.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        String url = "/files/" + today + "/" + filename; // WebConfig 매핑을 통해 공개 URL
        return Map.of(
                "url", url,
                "contentType", ctype,
                "size", file.getSize(),
                "originalName", Optional.ofNullable(file.getOriginalFilename()).orElse(filename)
        );
    }

    // (선택) 간단한 400 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadReq(IllegalArgumentException e) {
        return Map.of("message", e.getMessage());
    }

    @GetMapping("/shares/{code}")
    public LightstickShareDtos.ShareDetail getByCode(@PathVariable("code") String code) {
        return service.getByCode(code);
    }
}
