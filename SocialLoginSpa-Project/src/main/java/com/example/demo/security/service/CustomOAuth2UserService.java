package com.example.demo.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.dto.OAuthAttributes;
import com.example.demo.security.vo.CustomUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 🔐 OAuth2 로그인 사용자 정보를 처리하는 서비스
 * - Spring Security의 OAuth2 인증을 활용하여 사용자 정보를 가져옴
 * - 새로운 사용자라면 DB에 저장하고, 기존 사용자라면 정보를 로드함
 */
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository; // 사용자 정보를 저장하는 Repository

    /**
     * ✅ OAuth2 사용자 정보를 가져오는 메서드
     * - 사용자가 OAuth2 로그인 시 호출됨
     * - 제공업체(Google, GitHub, Naver 등)에서 사용자 정보를 받아와 DB에 저장하거나 로드
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser");

        // 🔹 기본 OAuth2 서비스 활용하여 사용자 정보 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 🔹 사용자가 로그인한 소셜 제공업체 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // ex) google, github, naver
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        log.info("loadUser registrationId = " + registrationId);
        log.info("loadUser userNameAttributeName = " + userNameAttributeName);

        // 🔹 OAuth2 제공업체에서 받은 사용자 정보를 변환
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String name = attributes.getName();
        String email = attributes.getEmail();
        String picture = attributes.getPicture();
        String id = attributes.getId();
        String socialType = "";

        // 🔹 제공업체별 사용자 정보 처리
        if ("naver".equals(registrationId)) {
            socialType = "naver";
        } else if ("kakao".equals(registrationId)) {
            socialType = "kakao";
        } else if ("github".equals(registrationId)) {
            socialType = "github";

            // GitHub 이메일이 null일 경우, API를 통해 가져오기
            if (email == null) {
                log.info("Fetching email from GitHub API...");
                email = getEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
            }
        } else {
            socialType = "google"; // 기본값 Google
        }

        log.info("Successfully retrieved user info: {}, {}", name, email);

        // 🔹 사용자 역할(Role) 설정 (기본적으로 ROLE_USER)
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 🔹 사용자 정보 DB 저장 (신규 유저라면 저장, 기존 유저라면 조회)
        UserEntity userEntity;
        if (!userRepository.existsByUsername(email)) {
            userEntity = UserEntity.builder()
                .username(email)
                .authProvider(socialType)
                .build();
            userEntity = userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByUsername(email);
        }

        // 🔹 최종적으로 사용자 객체를 반환 (Spring Security에서 인증된 객체로 사용)
        return new CustomUser(userEntity.getId(), email, name, authorities, attributes);
    }

    /**
     * ✅ GitHub API를 활용해 이메일 가져오기
     * - GitHub API는 기본적으로 사용자의 이메일을 OAuth 응답에서 제공하지 않을 수 있음
     * - 따라서 API 호출을 통해 이메일을 가져옴
     */
    private String getEmailFromGitHub(String accessToken) {
        String url = "https://api.github.com/user/emails";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map<String, Object>> emails = response.getBody();
        if (emails != null) {
            for (Map<String, Object> emailData : emails) {
                if ((Boolean) emailData.get("primary")) {
                    return (String) emailData.get("email"); // 기본 이메일 반환
                }
            }
        }

        return null; // 이메일 정보 없음
    }

}