package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserEntity;
import com.example.demo.security.vo.CustomUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 토큰생성 및 검증역할 수행
 */

@Service
public class TokenProvider {

	// JWT 서명에 사용할 비밀키(512비트 이상추천)
	private static final String SECRET_KEY = "FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

	// 비밀 키로부터 HMAC SHA키 객체 생성
	private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	
	// 사용자 기반 JWT 토큰 생성
	public String create(UserEntity userEntity) {
		// 토큰 만료시간(현재시간으로 1일)
		Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

		return Jwts.builder()
			.signWith(SIGNING_KEY, SignatureAlgorithm.HS512) // 서명 알고리즘 키
			.setSubject(String.valueOf(userEntity.getId())) // 사용자 ID를 Subject로 설정
			.setIssuer("demo app") // 토큰 발급자 정보 설정
			.setIssuedAt(new Date()) // 토큰 발급시간 설정
			.setExpiration(expiryDate) // 만료시간 설정
			.compact(); // 토큰생성 완료
	}

	// 토큰을 검증하고, 포함된 사용자 ID(subject)를 반환
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();

		return claims.getSubject();
	}

	// 사용자 ID를 사용하여 JWT 토큰 생성
	public String createByUserId(final Long userId) {
		// 토큰 만료시간(현재시간으로 1일)
		Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

		return Jwts.builder()
			.signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
			.setSubject(String.valueOf(userId))
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.compact();
	}
	
	public String create(final Authentication authentication) {
		CustomUser userPrincipal = (CustomUser) authentication.getPrincipal();

		Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

		return Jwts.builder()
			.setSubject(userPrincipal.getName())
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
			.compact();
	}
}
