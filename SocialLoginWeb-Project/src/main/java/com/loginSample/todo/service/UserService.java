package com.loginSample.todo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loginSample.todo.entity.User;
import com.loginSample.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드 생성자
@Service // 서비스 계층(스프링이 관리)
public class UserService {
	
	private final UserRepository userRepository; // 사용자관리 DB
	private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 디코더
	
	// 사용자 등록
	public User registerUser(String username, String password) {
		
		// 동일한 username 조회 후 예외처리
		if(userRepository.findByUsername(username).isPresent()) {
			throw new RuntimeException("존재하는 ID입니다.");
		}
		
		// User객체 생성
		User user = new User();
		user.setPassword(password);
		user.setPassword(passwordEncoder.encode(password)); // 사용자 비밀번호 암호화
		
		// 저장
		return userRepository.save(user);
	}
	
	// 사용자 이름으로 사용자 정보 조회
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
	}

}
