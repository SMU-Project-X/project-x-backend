package com.pix.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pix.dto.UserDto;
import com.pix.entity.MemberEntity;
import com.pix.entity.MyIdolEntity;
import com.pix.entity.MyIdolMemberEntity;
import com.pix.entity.UsersEntity;
import com.pix.entity.psychoTypeEntity;
import com.pix.service.EmailService;
import com.pix.service.EmailServiceImpl;
import com.pix.service.MemberService;
import com.pix.service.MyIdolMemberService;
import com.pix.service.MyIdolService;
import com.pix.service.PsychoTypeService;
import com.pix.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	private final EmailServiceImpl emailServiceImpl;
	
	@Autowired UserService userService;
	@Autowired MyIdolService myIdolService;
	@Autowired MemberService memberService;
	@Autowired PsychoTypeService psychoTypeService;
	@Autowired EmailService emailService; // 이메일 발송
	@Autowired MyIdolMemberService myIdolMemberService;

	@Autowired HttpSession session;
	
	UserController(EmailServiceImpl emailServiceImpl) {
		this.emailServiceImpl = emailServiceImpl;
	}
	
	@PostMapping("/api/signup/info")
	public ResponseEntity<String> info(@RequestBody Map<String, Object> payload) {
	    try {
	        // 1. 유저 저장
	        UsersEntity u = new UsersEntity();
	        u.setName((String) payload.get("userName"));
	        u.setUserId((String) payload.get("userId"));
	        u.setEmail((String) payload.get("email"));
	        u.setNickname((String) payload.get("nickname"));
	        u.setPasswordHash((String) payload.get("password"));
	        

	        // age 안전 캐스팅
	        Object ageObj = payload.get("age");
	        int age = (ageObj instanceof Integer) ? (Integer) ageObj : Integer.parseInt(ageObj.toString());
	        u.setAge(age);

	        UsersEntity savedUser = userService.save(u);

	        // 2. MyIdolEntity 생성
	        MyIdolEntity myIdol = new MyIdolEntity();
	        myIdol.setUserId(savedUser.getUserId());
	        MyIdolEntity savedMyIdol = myIdolService.save(myIdol);
	        
	        System.out.println(savedMyIdol);

	        // 3. 포지션 배열 (임시: index 기반)
	        String[] positions = {"보컬", "댄스", "랩", "비주얼"};

	        // 4. 프론트에서 보낸 selectedCharacters 꺼내기
	        List<Map<String, Object>> members =
	                (List<Map<String, Object>>) payload.get("selectedCharacters");
	        
////
	        for (int i = 0; i < members.size(); i++) {
		        Map<String, Object> m = members.get(i);
		        
		        System.out.println(m);
	
		        MyIdolMemberEntity idolMember = new MyIdolMemberEntity();
		        idolMember.setMyIdol(savedMyIdol);
		        idolMember.setPosition(positions[i]);
		        idolMember.setMbti((String) m.get("mbti"));
		        // MemberInfoEntity 매핑
		        String memberName = (String) m.get("name");
		        MemberEntity memberInfo = memberService.findByMemberName(memberName);
		        System.out.println("멤버인포: "+ memberInfo);
		        idolMember.setMemberInfo(memberInfo);
//	
		        // traits → psychoType 매핑
		        List<String> traits = (List<String>) m.get("traits");
		        psychoTypeEntity psychoType = psychoTypeService.findByTraits(traits);
		        idolMember.setPsychoType(psychoType);
		        
		        System.out.println(idolMember);
	
		        MyIdolMemberEntity myIdolMember = myIdolMemberService.save(idolMember);
		    }


	        return ResponseEntity.ok("회원가입 + MyIdol + 멤버 저장 완료");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("회원가입 정보 저장 실패: " + e.getMessage());
	    }
	}

	
	@PostMapping("/api/login") // 로그인 확인
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload, HttpSession session) {
	    String userId = payload.get("userId");
	    String password = payload.get("password");

	    UsersEntity user = userService.findByUserIdAndPassword(userId, password);

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
		
		UsersEntity user = userService.findByNameAndEmail(name, email);
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
