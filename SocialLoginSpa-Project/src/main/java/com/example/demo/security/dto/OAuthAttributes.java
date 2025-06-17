package com.example.demo.security.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth 제공자로부터 받은 사용자 정보
    private String nameAttributeKey; // 식별자의 key 값
    private String name; // 사용자 이름
    private String email; // 사용자 이메일
    private String picture; // 사용자 프로필 이미지
    private String id; // 사용자 고유 ID

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String id) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.id = id;
    }

    /**
     * OAuth 제공자의 타입을 기반으로 적절한 OAuthAttributes 객체를 생성하여 반환.
     * @param registrationId OAuth 제공자 ID (Google, Naver, Kakao, GitHub 등)
     * @param userNameAttributeName 제공자별 사용자 고유 식별자 키 값
     * @param attributes 제공자로부터 받은 사용자 정보
     * @return OAuthAttributes 객체
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        } else if ("github".equals(registrationId)) {
            return ofGitHub("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    /**
     * Google OAuth 사용자 정보 매핑
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .id((String) attributes.get(userNameAttributeName))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * Naver OAuth 사용자 정보 매핑
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .id((String) response.get(userNameAttributeName))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * Kakao OAuth 사용자 정보 매핑
     */
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Long id = (Long) attributes.get("id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .id(String.valueOf(id)) // Long -> String 변환
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * GitHub OAuth 사용자 정보 매핑
     */
    private static OAuthAttributes ofGitHub(String userNameAttributeName, Map<String, Object> attributes) {
        String username = (String) attributes.get("login");
        Integer id = (Integer) attributes.get("id");

        return OAuthAttributes.builder()
                .name(username)
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .id(String.valueOf(id)) // Integer -> String 변환
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}