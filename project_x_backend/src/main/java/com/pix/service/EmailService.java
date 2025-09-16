package com.pix.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	void emailSend(String email);

	boolean verifyEmailCode(String email, String verifyCode);

}
