package com.loginSample.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginSample.todo.entity.User;

// User 엔터티를 위한 JPA Repository 인터페이스
public interface UserRepository extends JpaRepository<User, Long> {
	
	// 사용자 이름으로 사용자 정보 조회(존재하지 않을 수 있으므로 Optional로 감싸 반환 
	Optional<User> findByUsername(String username);
}
