package com.loginSample.todo.security.vo;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.loginSample.todo.security.dto.OAuthAttributes;

import lombok.Getter;

// Spring Security에서 사용자 정보 담는 클래스

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
	
	private static final long serialVersionUID = 1L; // 직렬화 버전 UID
	
	private Long userId; // 사용자고유 ID
	private String username; // 사용자 이름(예: 이메일)
	private String password; // 사용자 비밀번호
	private String nickname; // 사용자 닉네임
	
	private Collection<? extends GrantedAuthority> authorities; // 사용자 권한목록
	private Map<String, Object> attributes;  // OAuth2 사용자 정보
	
	// 일반 로그인 사용자 생성자
    public CustomUserDetails(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
    	this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.attributes = null;  // 일반 로그인 사용자는 OAuth2 속성이 없음
    }

    // OAuth2 로그인 사용자 생성자
    public CustomUserDetails(Long userId, String username, Collection<? extends GrantedAuthority> authorities, OAuthAttributes oAuthAttributes) {
    	this.userId = userId;
        this.username = username;
        this.authorities = authorities;
        this.attributes = oAuthAttributes.getAttributes();
        this.password = null;  // OAuth2 사용자는 비밀번호가 없음
    }
	
    @Override
	public String getName() {
		return username;  // OAuth2User에서 사용하는 name 속성
	}
    
	// 사용자 권한정보 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
