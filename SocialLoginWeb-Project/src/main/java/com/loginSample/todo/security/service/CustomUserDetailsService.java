package com.loginSample.todo.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.loginSample.todo.entity.User;
import com.loginSample.todo.repository.UserRepository;
import com.loginSample.todo.security.vo.CustomUserDetails;


@Service // Service 컴포넌트
// Spring Security 에서 사용자 인증 정보 조회 클래스
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// username(email)을 기준으로 사용자 정보를 로딩하는 메소드
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.err.println("사용자 정보 조회"); // 디버깅용 출력
		
		// username을 이용하여 사용자 정보 조회
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		// 사용자 정보 추출
		Long userid = user.getId();
		String email = user.getUsername();
		String password = user.getPassword();
		
		// 권한리스트 생성 및 권한추가
		List<SimpleGrantedAuthority> authorites = new ArrayList<>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorites.add(authority);
		
		// 사용자정보를 담은 CustomUserDetails 객체생성 및 반환
		return new CustomUserDetails(userid, username, password, authorites);
	}
	

}
