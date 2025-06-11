package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 전달 DTO
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private String token; // 로그인 인증 성공시 사용하는 JWT토큰
	private String username;
	private String password;
	private Long id;
	
}
