package com.pix.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.pix.dto.UserDto;
import com.pix.entity.UsersEntity;
import com.pix.service.EmailService;
import com.pix.service.EmailServiceImpl;
import com.pix.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	private final EmailServiceImpl emailServiceImpl;
	
	@Autowired UserService userService;
	@Autowired HttpSession session;
	@Autowired EmailService emailService; // 이메일 발송
	
	UserController(EmailServiceImpl emailServiceImpl) {
		this.emailServiceImpl = emailServiceImpl;
	}
	
	// ===== 기존 API들 (그대로 유지) =====
	
	@PostMapping("/api/signup/info")
    public ResponseEntity<String> info(@RequestBody Map<String, String> payload) {
		try {
			UsersEntity u = new UsersEntity();
	    	
			// payload 안에 JSON 데이터가 들어옴
	        String name = payload.get("userName");
	        String userid = payload.get("userId");
	        String email = payload.get("email");
	        String age_str = payload.get("age");
	        int age = Integer.parseInt(age_str);     
		    
	        String password = payload.get("password");
	        String nickname = payload.get("nickname");

	        System.out.println("username = " + name);
	        System.out.println("userid = " + userid);
	        System.out.println("password = " + password);
	        System.out.println("email = " + email);
	        System.out.println("age =" + age);
	        System.out.println("nickname =" + nickname);
	        
	        u.setName(name);
	        u.setUserId(userid);
	        u.setEmail(email);
	        u.setAge(age);
	        u.setPasswordHash(password);
	        u.setNickname(nickname);
	        
	        UsersEntity user = userService.save(u);

	     // 성공하면 200 OK와 메시지
	        return ResponseEntity.ok("회원가입 정보 저장 완료");
	    } catch (Exception e) {
	        // 문제가 생기면 400 또는 500 에러와 메시지
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("회원가입 정보 저장 실패");
	    }
	}
	
	//  🔥 수정된 로그인 API (MD 페이지 호환 + 관리자 권한 체크)
	@PostMapping("/api/login")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload, HttpSession session) {
		try {
			// username 또는 userId 모두 처리 (MD 페이지 호환)
			String identifier = payload.get("username");
			if (identifier == null) {
				identifier = payload.get("userId");
			}
			String password = payload.get("password");
			
			System.out.println("로그인 시도: " + identifier);
			
			UsersEntity user = userService.findByUserIdAndPassword(identifier, password);
			
			if (user == null) {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("success", false);
				errorResponse.put("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			} else {
				// 🔥 관리자 권한 체크 - DB의 is_admin 값을 확인
				boolean isAdmin = "1".equals(user.getIsAdmin()) || "Y".equals(user.getIsAdmin()) || 
				                "true".equalsIgnoreCase(user.getIsAdmin());
				
				// 세션에 로그인 정보 저장 (기존 방식 + MD 호환)
				session.setAttribute("session_id", user.getUserId()); // 기존 방식
				session.setAttribute("session_name", user.getName()); // 기존 방식
				
				// MD 페이지 호환을 위한 새로운 세션 키들
				session.setAttribute("isLoggedIn", true);
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("username", user.getName());
				session.setAttribute("name", user.getName());
				session.setAttribute("isAdmin", isAdmin); // 🔥 실제 관리자 권한 설정
				
				// MD 페이지 응답 형식
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("message", "로그인 성공");
				
				// 사용자 정보 객체
				Map<String, Object> userInfo = new HashMap<>();
				userInfo.put("userId", user.getUserId());
				userInfo.put("username", user.getName());
				userInfo.put("name", user.getName());
				userInfo.put("isAdmin", isAdmin); // 🔥 실제 관리자 권한 설정
				response.put("user", userInfo);
				
				System.out.println("로그인 성공: " + user.getName() + " (관리자: " + isAdmin + ")");
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			System.err.println("로그인 처리 중 오류: " + e.getMessage());
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "로그인 처리 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	
	// ===== MD 페이지 헤더를 위한 새로운 API들 =====
	
	/**
	 * 🚀 로그인 상태 확인 API (MD 헤더에서 사용) - 수정
	 */
	@GetMapping("/api/users/status")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getLoginStatus(HttpSession session) {
		try {
			// 기존 세션 키로 확인
			String sessionUserId = (String) session.getAttribute("session_id");
			String sessionName = (String) session.getAttribute("session_name");
			
			// 새로운 세션 키로도 확인
			Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
			String userId = (String) session.getAttribute("userId");
			String username = (String) session.getAttribute("username");
			String name = (String) session.getAttribute("name");
			Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
			
			Map<String, Object> response = new HashMap<>();
			
			// 기존 방식 또는 새로운 방식 중 하나라도 로그인되어 있으면 인정
			if (sessionUserId != null || (isLoggedIn != null && isLoggedIn)) {
				// 🔥 DB에서 최신 사용자 정보를 다시 확인 (관리자 권한 변경 반영)
				String currentUserId = sessionUserId != null ? sessionUserId : userId;
				try {
					UsersEntity currentUser = userService.findByUserId(currentUserId);
					if (currentUser != null) {
						boolean realIsAdmin = "1".equals(currentUser.getIsAdmin()) || 
						                    "Y".equals(currentUser.getIsAdmin()) || 
						                    "true".equalsIgnoreCase(currentUser.getIsAdmin());
						
						// 세션 업데이트
						session.setAttribute("isAdmin", realIsAdmin);
						
						response.put("isLoggedIn", true);
						response.put("userId", currentUser.getUserId());
						response.put("username", currentUser.getName());
						response.put("name", currentUser.getName());
						response.put("isAdmin", realIsAdmin);
					} else {
						// 사용자를 찾을 수 없으면 로그아웃 처리
						response.put("isLoggedIn", false);
						response.put("userId", null);
						response.put("username", null);
						response.put("name", null);
						response.put("isAdmin", false);
					}
				} catch (Exception e) {
					// DB 조회 실패시 기존 세션 정보 사용
					response.put("isLoggedIn", true);
					response.put("userId", sessionUserId != null ? sessionUserId : userId);
					response.put("username", sessionName != null ? sessionName : username);
					response.put("name", sessionName != null ? sessionName : name);
					response.put("isAdmin", isAdmin != null ? isAdmin : false);
				}
			} else {
				response.put("isLoggedIn", false);
				response.put("userId", null);
				response.put("username", null);
				response.put("name", null);
				response.put("isAdmin", false);
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.err.println("로그인 상태 확인 오류: " + e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("isLoggedIn", false);
			response.put("userId", null);
			response.put("username", null);
			response.put("name", null);
			response.put("isAdmin", false);
			return ResponseEntity.ok(response);
		}
	}
	
	/**
	 * 🚀 로그아웃 API
	 */
	@PostMapping("/api/users/logout")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
		try {
			session.invalidate(); // 세션 무효화
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "로그아웃 되었습니다.");
			
			System.out.println("로그아웃 처리 완료");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.err.println("로그아웃 처리 오류: " + e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "로그아웃 처리되었습니다.");
			return ResponseEntity.ok(response);
		}
	}
	
	// ===== 기존 API들 (그대로 유지) =====
	
	@PostMapping("/api/findId")
	@ResponseBody
	public ResponseEntity<Map<String, String>> findId(@RequestBody Map<String, String> payload) {
		String name = payload.get("name");
		String email = payload.get("email");
		
		try {
			UsersEntity user = userService.findByNameAndEmail(name, email);
			if (user != null) {
				return ResponseEntity.ok(Map.of("message", "아이디 찾기 성공", "userId", user.getUserId()));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("message", "해당 정보로 등록된 사용자를 찾을 수 없습니다."));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "아이디 찾기 처리 중 오류가 발생했습니다."));
		}
	}
	
	@ResponseBody
	@PostMapping("/api/signup/emailSend") //email인증번호 발송
	public String emailSend(@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
		
		System.out.println("email : "+email);
		// 이메일전송 구현
		emailService.emailSend(email);
		
		return "success";
	}
	
	@PostMapping("/api/signup/emailVerify") //인증번호 인증
	@ResponseBody
	public ResponseEntity<Map<String, Object>> verifyEmailCode(@RequestBody Map<String, String> payload) {
		String verifyCode = payload.get("verifyCode");
		String email = payload.get("email");
		
		System.out.println("verifyCode : "+verifyCode);
		
		// 이메일전송 후 인증코드 비교
		boolean isVerified = emailService.verifyEmailCode(email, verifyCode);
		
		if (isVerified) {
	        // 인증 성공 시
	        return ResponseEntity.ok(
	            Map.of(
	                "status", "success",
	                "message", "이메일 인증에 성공했습니다.",
	                "email", email
	            )
	        );
	    } else {
	        // 인증 실패 시
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	            Map.of(
	                "status", "fail",
	                "message", "인증번호가 일치하지 않습니다."
	            )
	        );
	    }
	}
}