package com.loginSample.todo.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.loginSample.todo.entity.User;
import com.loginSample.todo.repository.UserRepository;
import com.loginSample.todo.security.dto.OAuthAttributes;
import com.loginSample.todo.security.vo.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2 로그인 후 사용자 정보를 처리하는 클래스 
 */

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private final UserRepository userRepository;
	// SecurityConfig에서 주입받는 대신 직접 생성하여 사용(순환참조 방지)
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
	// OAuth2로그인 시 사용자 정보를 불러오고 처리하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	log.info("loadUser");
    	
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        
        log.info("loadUser registrationId = " + registrationId);
        log.info("loadUser userNameAttributeName = " + userNameAttributeName);
        
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String nameAttributeKey = attributes.getNameAttributeKey();
        String name = attributes.getName();
        String email = attributes.getEmail();
        String picture = attributes.getPicture();
        String id = attributes.getId();
        String socialType = "";
        
        if("naver".equals(registrationId)) {
        	socialType = "naver";
        }
        else if("kakao".equals(registrationId)) {
        	socialType = "kakao";
        }
        else if("github".equals(registrationId)) {
        	socialType = "github";
        	
        	if(email == null) {
        		log.info("loadUser userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
            	
        		email = getEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
            	
            	log.info("loadUser GitHub email = " + email);
        	}
        }
        else {
        	socialType = "google";
        }
        
        log.info("loadUser nameAttributeKey = " + nameAttributeKey);
        log.info("loadUser id = " + id);
        log.info("loadUser socialType = " + socialType);
        log.info("loadUser name = " + name);
        log.info("loadUser email = " + email);
        log.info("loadUser picture = " + picture);
        
        log.info("loadUser attributes = " + attributes);
        
        if(name == null) name = "";
        if(email == null) email = "";
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);
        
        Optional<User> optionalUser = userRepository.findByUsername(email);
        
        User createdUser = null;
        if(optionalUser.isEmpty()) {
        	User user = new User();
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode("1234"));
            user.setSocialId(id);
            user.setSocialType(socialType);
            
            createdUser = userRepository.save(user);
        }
        else {
        	createdUser = optionalUser.orElseThrow();
        }
        
        Long userId = createdUser.getId();
        
        return new CustomUserDetails(userId, email, authorities, attributes);
    }
    
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
                    return (String) emailData.get("email");
                }
            }
        }
        return null;
    }
    
}
