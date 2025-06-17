package com.example.demo.security.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.example.demo.security.dto.OAuthAttributes;

/**
 * Spring Security의 DefaultOAuth2User를 확장한 CustomUser 클래스.
 * OAuth 인증 정보를 기반으로 사용자 정보를 추가적으로 관리하기 위한 역할을 수행.
 */
public class CustomUser extends DefaultOAuth2User {

    private static final long serialVersionUID = 1L; // 직렬화 시 UID를 명시하여 버전 관리

    private Long id; // 사용자 고유 ID
    private String email; // 사용자 이메일
    private String username; // 사용자 이름

    /**
     * CustomUser 생성자
     * @param id 사용자 ID
     * @param email 사용자 이메일
     * @param username 사용자 이름
     * @param authorities 사용자의 권한 정보
     * @param attributes OAuthAttributes 객체 (OAuth 제공자로부터 받은 사용자 정보)
     */
    public CustomUser(Long id, String email, String username, Collection<? extends GrantedAuthority> authorities, OAuthAttributes attributes) {
        super(authorities, attributes.getAttributes(), attributes.getNameAttributeKey()); // DefaultOAuth2User 초기화

        this.id = id;
        this.email = email;
        this.username = username;
    }

    /**
     * 사용자 이메일 반환
     * @return 사용자 이메일
     */
    public String getEmail() {
        return email;
    }    

    /**
     * 사용자 이름 반환
     * @return 사용자 이름
     */
    public String getUsername() {
        return username;
    }

    /**
     * Spring Security의 getName() 메서드 오버라이드
     * @return 사용자 ID를 문자열로 반환
     */
    @Override
    public String getName() {
        return "" + this.id;
    }

}