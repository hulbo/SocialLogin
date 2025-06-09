package com.loginSample.todo.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j // 로그 객체 생성
// 로그인 성공 시 실행되는 커스텀 핸들러 클래스
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	// 로그인 성공시 사용되는 콜백 메소드
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request // 클라이언트 요청 
			,HttpServletResponse response // 서버 응답
			, Authentication authentication // 인증된 사용자 정보
			) throws IOException, ServletException {
		
		// 성공로그출력
		log.info("onAuthenticationSuccess");
		
		String targetUrl = "/"; // 로그인 성공 후 이동할 기본 URL설정
		response.sendRedirect(targetUrl); // 해당 URL로 리다이렉트
		
		// 사용자 권한에 따라 페이지 이동, JWT인경우 생성후 응답에 추가 등 작업
		
	}
	
}
