package com.loginSample.todo.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그객체 자동생성
// 로그인 실패 시 실행되는 커스텀 핸들러
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	// 인증 요청이 저장된 캐시에서 이전 요청을 가져오기 위한 객체
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	// 로그인 실패 시 호출되는 메서드
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request // 클라이언트 요청 
			,HttpServletResponse response // 서버 응답
			,AuthenticationException exception // 인증 실패시 예외 정보
			) throws IOException, ServletException {

		// 로그인 실패 예외를 로그로 출력
		log.info("로그인 실채 exception : " + exception);
		
		// 사용자가 원례 가려던 요청 정보 가져오기
		SavedRequest saveRequest = requestCache.getRequest(request, response);
		
		if(saveRequest != null) {
			// 요청URL
			String targetUrl = saveRequest.getRedirectUrl();
			
			// 실패시 리다이렉트 URL로그 출력
			log.info("Login failure targetURL :: " + targetUrl);
			
			
			// 사용자가 가려던 URL로 리다이렉트(로그인 실패 후에도 이동)
			response.sendRedirect(targetUrl);
			
		}
		
	}

}
