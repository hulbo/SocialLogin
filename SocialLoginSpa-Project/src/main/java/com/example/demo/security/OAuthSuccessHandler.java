package com.example.demo.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.example.demo.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * OAuth2 로그인 성공 핸들러 - 로그인 후 JWT 토큰을 생성하고 사용자에게 리다이렉트
 */
@Slf4j
@AllArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000"; // 기본 리다이렉션 주소

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        
        // 1. JWT 토큰 생성
        TokenProvider tokenProvider = new TokenProvider();
        String token = tokenProvider.create(authentication);
        log.info("token {}", token);

        // 2. 쿠키에서 redirect_url 가져오기
        Optional<Cookie> oCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM))
            .findFirst();
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);

        log.info("redirectUri {}", redirectUri);

        // 3. 리다이렉트할 최종 URL 구성
        String targetUrl = redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL) + "/sociallogin?token=" + token;
        log.info("targetUrl {}", targetUrl);

        // 4. 사용자 브라우저를 최종 URL로 리다이렉트 (로그인 완료 후 토큰 전달)
        response.sendRedirect(targetUrl);
    }

}