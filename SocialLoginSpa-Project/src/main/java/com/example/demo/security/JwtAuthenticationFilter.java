
package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 요청에서 JWT 토큰을 추출하고 인증 정보를 설정하는 필터 클래스
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 이걸 상속하면 HTTP 요청당 한 번만 실행되는 필터

	// 토큰 검증용 Provider
	private final TokenProvider tokenProvider;

	// OPTIONS 요청은 필터를 건너뛰도록 설정
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (request.getMethod().equals("OPTIONS")) {
			// 필터 작동
			return true;
		}
		// 필터 작동안함
		return false;
	}

	// 필터내부로직: JWT토큰 파싱 -> 검증 -> 인증객체생성 -> SecurityContext 설정
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// JWT토큰 파싱
			String token = parseBearerToken(request);
			
			log.info("doFilterInternal");
			if (token != null && !token.equalsIgnoreCase("null")) {
				// 토큰 검증
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated user ID : " + userId);
				
				// 인증객체생성(권한없음)
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId, null, AuthorityUtils.NO_AUTHORITIES);
				
				// 요청정보 추가
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// 새로운 SecurityContext 생성 및 인증 객체 설정
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);
				
				// 현재 쓰레드에 SecurityContext등록
				SecurityContextHolder.setContext(securityContext);
			}
		} catch (Exception ex) {
			log.error("Could not set user authentication in security context", ex);
		}

		// 다음필터 실행
		filterChain.doFilter(request, response);
	}

	// JWT토큰 파싱
	private String parseBearerToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
}
