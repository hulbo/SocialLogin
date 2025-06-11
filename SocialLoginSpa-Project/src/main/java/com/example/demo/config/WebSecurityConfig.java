package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.OAuthSuccessHandler;
import com.example.demo.security.RedirectUrlCookieFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final OAuthSuccessHandler oAuthSuccessHandler;
    
    private final RedirectUrlCookieFilter redirectUrlFilter;
    
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter
    		, OAuthSuccessHandler oAuthSuccessHandler
    		, RedirectUrlCookieFilter redirectUrlFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
        this.redirectUrlFilter = redirectUrlFilter;
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .cors(cors -> {}) // CORS 설정 활성화
        .csrf(csrf -> csrf.disable()) // CRSF 비활성화 (REST-API 에서 주로 사용)
        .httpBasic(httpBasic -> httpBasic.disable()) // 기본인증방식 비활성화
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용안함
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/auth/**").permitAll() // 루트 및 /auth/** 경로는 인증없이 사용
            .anyRequest().authenticated() // 나머지는 인증필요
        )
        // JWT인증필터 를 필터체인에 추가한다.
        .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oauth2 -> oauth2
            .successHandler(oAuthSuccessHandler) // 성공 핸들러
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint()) // 인증실패시 403 
        ) 
        // 필터체인 추가
        .addFilterBefore(redirectUrlFilter, OAuth2AuthorizationRequestRedirectFilter.class);

        return http.build();
    }

    // 접근 CORS 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 자격 증명 포함 허용(예: 쿠키, Authorization 헤더)
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 프로트엔드 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 허용 메소드
        configuration.setAllowedHeaders(List.of("*")); // 모든 요청 헤더 허용
        configuration.setExposedHeaders(List.of("*")); // 응답 헤더 노출
        
        // 위의 CORS설정을 모든 경로에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
