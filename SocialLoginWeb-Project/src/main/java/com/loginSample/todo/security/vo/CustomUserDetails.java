package com.loginSample.todo.security.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;

// Spring Security에서 사용자 정보 담는 클래스

@RequiredArgsConstructor // final 필드 생성자
@Data
public class CustomUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L; // 직렬화 버전 UID
	
	private final Long userId; // 사용자고유 ID
	private final String username; // 사용자 이름(예: 이메일)
	private final String password; // 사용자 비밀번호

	// 사용자 권한목록
	private final Collection<? extends GrantedAuthority> authority;
	
	// 사용자 권한정보 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authority;
	}

	// 사용자 비밀번호 반환
	@Override
	public String getPassword() {
		return password;
	}

	// 사용자 이름 반환
	@Override
	public String getUsername() {
		return username;
	}

	// 계정 만료 여부(true = 만료되지 않음)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 계정 잠김 여부(true = 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// 자격 증명(비밀번호)만료 여부 
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 비밀번호 만료 검사 생략
	}
	
	// 계정 활성화 여부
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 커스텀 메소드: 사용자ID 반환
	public Long getUserid() {
		return userId;
	}
	
}
