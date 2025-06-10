package com.loginSample.todo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.loginSample.todo.security.handler.CustomLoginFailureHandler;
import com.loginSample.todo.security.handler.CustomLoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration // 설정 클래스임을 Spring에게 알림
@EnableWebSecurity // Spring Security 웹 보안 활성화
public class SecurityConfig {
	
	// OAuth2 로그인 사용자 정보 처리 서비스
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
	
	private final UserDetailsService customUserDetailsService;
	
	// 비밀번호 암호화를 위한 Bean등록
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // BCrypt 알고리즘을 사용한 passwordEncoder 반환
	}
	
	// SecurityFilterChain: 보안 설정의 핵심 구성 요송
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		// URL별 접근 권한설정
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/users/register", "/login", "/css/**", "/js/**").permitAll() // 누구나 접근 허용
				.anyRequest().authenticated() // 그 외의 요청은 인증 필요
		)
		.userDetailsService(customUserDetailsService)
		// 폼 로그인 설정
		.formLogin(form -> form
				.loginPage("/login") // 커스텀 로그인 페이지 경로
				.permitAll() // 로그인 페이지는 인증없이 접근가능
				//.defaultSuccessUrl("/todos", true) // 로그인 성공 시 "/todos"로 리다이렉트(항상)
				.successHandler(authenticationSuccessHandler()) // 성공 핸들러
				.failureHandler(authenticationFailureHandler()) // 실패 핸들러
		)
		// OAuth2 로그인 설정
		.oauth2Login(oauth2 -> oauth2
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
	            .userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
				)
	        )
		// 로그아웃 설정
		.logout(logout -> logout
				.logoutUrl("/logout") // 로그아웃 URL
				.logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 이동한 URL
				.permitAll()
		)
		;
		
		// 설정완료 후 SecurityFilterChain 반환
		return http.build();
	}
	
	// 로그인 성공 시 실행되는 핸들러 Bean등록
	@Bean
	AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	// 로그인 실패 시 실행되는 핸들러 Bean등록
	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomLoginFailureHandler();
	}
}
