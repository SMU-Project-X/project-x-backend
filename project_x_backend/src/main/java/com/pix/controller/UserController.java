package com.pix.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pix.dto.User;
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
	
	@PostMapping("/api/signup/info")
    public ResponseEntity<String> info(@RequestBody Map<String, String> payload) {
		try {
			User u = new User();
	    	
			// payload 안에 JSON 데이터가 들어옴
	        String name = payload.get("userName");
	        String userid = payload.get("userId");
	        String email = payload.get("email");
	        String birthdayStr = payload.get("birthdate");
		    // 문자열 → LocalDate 변환
		    LocalDate birthday = LocalDate.parse(birthdayStr);
		    
	        String gender = payload.get("gender");
	        String password = payload.get("password");
	        String nickname = payload.get("nickname");
	
	        System.out.println("username = " + name);
	        System.out.println("userid = " + userid);
	        System.out.println("password = " + password);
	        System.out.println("email = " + email);
	        System.out.println("birthday =" + birthday);
	        System.out.println("gender =" + gender);
	        System.out.println("nickname =" + nickname);
	        
	        u.setName(name);
	        u.setUserId(userid);
	        u.setEmail(email);
	        u.setBirthday(birthday);
	        u.setGender(gender);
	        u.setPassword(password);
	        u.setNickname(nickname);
	        
	        User user = userService.save(u);
	
	     // 성공하면 200 OK와 메시지
	        return ResponseEntity.ok("회원가입 정보 저장 완료");
	    } catch (Exception e) {
	        // 문제가 생기면 400 또는 500 에러와 메시지
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("회원가입 정보 저장 실패");
	    }
		
	}
	
	@PostMapping("/api/login") // 로그인 확인
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload, HttpSession session) {
	    String userId = payload.get("userId");
	    String password = payload.get("password");

	    User user = userService.findByUserIdAndPassword(userId, password);

	    if (user == null) {
	        // 실패도 Map으로 반환
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(Map.of("message", "아이디 또는 비밀번호가 일치하지 않습니다."));
	    } else {
	        session.setAttribute("session_id", user.getUserId());
	        session.setAttribute("session_name", user.getName());
	        return ResponseEntity.ok(Map.of("message", "로그인 성공", "userId", user.getUserId()));
	    }
	}
	
	public ResponseEntity<Map<String, String>> findId(@RequestBody Map<String, String> payload) {
		String name = payload.get("name");
		String email = payload.get("email");
		
		User user = userService.findByNameAndEmail(name, email);
		return ResponseEntity.ok(Map.of("message", "로그인 성공", "userId", user.getUserId()));
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
