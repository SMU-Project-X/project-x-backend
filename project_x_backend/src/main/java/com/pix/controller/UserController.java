package com.pix.controller;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pix.dto.User;
import com.pix.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired UserService userService;
	@Autowired HttpSession session;
	
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
}
