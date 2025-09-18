package com.pix.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	@Autowired JavaMailSender mailSender;

	// 이메일별 인증 코드 저장 (실제 운영에서는 Redis 같은 캐시를 쓰는 게 안전)
    private Map<String, String> verifyCodeStorage = new HashMap<>();

	@Override // 이메일전송 구현
	@Async    // 비동기 방식으로 전환
	public void emailSend(String email) {
		String pwCode = getCreateKey();
		System.out.println("랜덤번호 : "+ pwCode);
		
		// 메모리에 저장
        verifyCodeStorage.put(email, pwCode);
        
		// 네이버 이메일 발송구현
		// TEXT로 메일 발송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email); // 보내는 주소
		message.setFrom("ydjxlst@naver.com"); //네이버이메일->네이버메일 에러가 남.
		message.setSubject("[Project-X 회원가입 메일입니다.]");  // 메일 제목
		message.setText("인증을 진행해 주세요. 랜덤번호 : "+pwCode+"\n"+
		"[ 서비스를 이용해주셔서 감사합니다. ]");  // JavaMailSender 글자전송만 가능
		mailSender.send(message);
		System.out.println("이메일 전송이 완료되었습니다.");
//				
		
	}
	
	// 랜덤번호 생성 메소드
		public String getCreateKey() {
			String pwCode = "";
			char[] charSet = new char[] {
					'0','1','2','3','4','5','6','7','8','9',
					'A','B','C','D','E','F','G','H','I','J',
					'K','L','M','N','O','P','Q','R','S','T',
					'U','V','W','X','Y','Z'
			};
//			String stringSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			
			int idx = 0;
			for(int i=0;i<10;i++) {
				idx = (int)(Math.random()*36);
				pwCode += charSet[idx];
			}
			return pwCode;
		}

		@Override
		public boolean verifyEmailCode(String email, String verifyCode) {
			String savedCode = verifyCodeStorage.get(email);
	        if (savedCode != null && savedCode.equals(verifyCode)) {
	            return true; // 인증 성공
	        }
	        return false; // 인증 실패
			
		}

}
