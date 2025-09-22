package com.pix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //자동으로 객체선언 - controller,service,repository,configuration,bean,component
public class fileUpload_config implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//URL 링크가 /upload/ 들어오면 c:upload 폴더에서 찾음
		// 설정이 없으면 무조건 static 에서 찾게 됨.
		registry.addResourceHandler("/upload/**")
		.addResourceLocations("file:/Users/ryujaeeun/Downloads/upload/");
		
	}
}
