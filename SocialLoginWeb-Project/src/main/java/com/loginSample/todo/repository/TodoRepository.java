package com.loginSample.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginSample.todo.entity.Todo;

// Todo 엔터티를 위한 JPA Repository 인터페이스
public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	// ID에 해당하는 할 일 목록 조회 메소드
	List<Todo> findByUserId(Long userId);
}
