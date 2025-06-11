package com.example.demo.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2 인증 요청 시, redirect_url 파라미터를 쿠키에 저장하는 필터
 */
@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {

    public static final String REDIRECT_URI_PARAM = "redirect_url"; // 쿠키에 저장할 리디렉션 URL 파라미터명
    private static final int MAX_AGE = 180; // 쿠키 유지 시간 (초 단위, 3분)

    /**
     * Spring Security 필터 체인에서 실행되는 메서드
     * 특정 요청(/oauth2/authorization)에 대해 redirect_url을 쿠키에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // OAuth2 인증 요청이 들어온 경우만 실행
        if (request.getRequestURI().startsWith("/oauth2/authorization")) {
            try {
                log.info("request uri {} ", request.getRequestURI());

                // 클라이언트 요청에서 redirect_url 파라미터 가져오기
                String redirectUrl = request.getParameter(REDIRECT_URI_PARAM);

                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl);
                cookie.setPath("/"); // 모든 경로에서 접근 가능
                cookie.setHttpOnly(true); // JavaScript에서 접근 불가 (보안 강화)
                cookie.setMaxAge(MAX_AGE); // 3분 후 자동 삭제
                response.addCookie(cookie); // 응답에 쿠키 추가

            } catch (Exception ex) {
                log.error("Could not set user authentication in security context", ex);
                log.info("Unauthorized request");
            }
        }

        // 필터 체인을 이어서 실행 (다음 필터 또는 컨트롤러로 요청 전달)
        filterChain.doFilter(request, response);
    }
}
