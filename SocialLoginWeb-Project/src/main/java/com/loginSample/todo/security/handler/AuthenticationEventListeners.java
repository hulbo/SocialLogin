package com.loginSample.todo.security.handler;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j // log변수 자동생성
@Component // 스프링관리 컴포넌트 생성
// 스프링 시큐리티 인증 관련 이벤트를 수신하여 처리하는 리스너
// 비동기 방식으로 로그성 처리를 하는게 좋다(로그인 처리에 영향 받지 않음)
public class AuthenticationEventListeners {
	
	// 모든 인증 이벤트의 공통처리
	@EventListener
	public void handlerAuthenticationEvent(AbstractAuthenticationEvent event) {
		log.info("handlerAuthenticationEvent ::: " + event);
	}

	// 로그인 실패 이벤트 처리(잘못된 자격 증명)
	@EventListener
	public void handlerBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
		// 로그인 실패시 실패횟수 등 추가 처리 시 사용함
		log.info("handlerBadCredentials");
	}

	// 로그인 성공 이벤트 처리
	@EventListener
	public void handlerAuthenticationSuccess(AuthenticationSuccessEvent event) {
		// 마지막 로그인 시간 기록, 알림 메시지, 로그인 실패 횟수 초기화등 처리시 사용함
		log.info("handlerAuthenticationSuccess");
	}
	
	
}
