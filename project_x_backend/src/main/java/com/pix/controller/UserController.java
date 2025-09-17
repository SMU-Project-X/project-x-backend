package com.pix.controller;

import com.pix.dto.UserDto;
import com.pix.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(
            @Valid @RequestBody UserDto.LoginRequest request, 
            HttpSession session) {
        
        log.info("로그인 요청 - 사용자: {}", request.getUsername());
        
        try {
            UserDto.LoginResponse response = userService.login(request);
            
            if (response.isSuccess()) {
                // 세션에 사용자 정보 저장
                session.setAttribute("userId", response.getUser().getUserId());
                session.setAttribute("username", response.getUser().getUsername());
                session.setAttribute("isAdmin", response.getUser().getIsAdmin());
                session.setAttribute("isLoggedIn", true);
                
                log.info("로그인 성공 - 사용자: {}, 세션 ID: {}", 
                        response.getUser().getUsername(), session.getId());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("로그인 처리 중 오류", e);
            UserDto.LoginResponse errorResponse = UserDto.LoginResponse.builder()
                    .success(false)
                    .message("로그인 처리 중 오류가 발생했습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 로그아웃 API
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        String username = (String) session.getAttribute("username");
        log.info("로그아웃 요청 - 사용자: {}", username);
        
        session.invalidate(); // 세션 무효화
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "로그아웃되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserDto.RegisterRequest request) {
        log.info("회원가입 요청 - 사용자: {}", request.getUsername());
        
        try {
            UserDto.Response user = userService.register(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("user", user);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 로그인 상태 확인 API
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLoginStatus(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        Map<String, Object> response = new HashMap<>();
        
        if (isLoggedIn != null && isLoggedIn) {
            response.put("isLoggedIn", true);
            response.put("userId", userId);
            response.put("username", username);
            response.put("isAdmin", isAdmin != null ? isAdmin : false);
        } else {
            response.put("isLoggedIn", false);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 정보 조회 API
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto.Response> getUserById(@PathVariable Long userId) {
        try {
            UserDto.Response user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            log.warn("사용자 조회 실패: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자 정보 수정 API
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long userId, 
            @Valid @RequestBody UserDto.UpdateRequest request,
            HttpSession session) {
        
        try {
            // 세션에서 현재 로그인한 사용자 확인
            Long sessionUserId = (Long) session.getAttribute("userId");
            Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
            
            // 본인 또는 관리자만 수정 가능
            if (sessionUserId == null || (!sessionUserId.equals(userId) && !Boolean.TRUE.equals(isAdmin))) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            UserDto.Response updatedUser = userService.updateUser(userId, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용자 정보가 수정되었습니다.");
            response.put("user", updatedUser);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("사용자 수정 실패: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 사용자명 중복 체크 API
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String username) {
        boolean exists = userService.isUsernameExists(username);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 사용자명입니다." : "사용 가능한 사용자명입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 중복 체크 API
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmail(@PathVariable String email) {
        boolean exists = userService.isEmailExists(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 사용자 목록 조회 API (관리자용)
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<UserDto.Response>> getAllUsers(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (!Boolean.TRUE.equals(isAdmin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<UserDto.Response> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 통계 조회 API (관리자용)
     */
    @GetMapping("/admin/stats")
    public ResponseEntity<UserDto.UserStats> getUserStats(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (!Boolean.TRUE.equals(isAdmin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserDto.UserStats stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", e.getMessage());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("서버 오류 발생: {}", e.getMessage(), e);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "서버 오류가 발생했습니다.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}