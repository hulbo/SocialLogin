package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TodoEntity;

/*
 * TodoEntity를 관리하는 JPA 인터페이스 
 */

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
	
	// 특정 사용자의 TODO
	List<TodoEntity> findByUserId(Long userId);

	// 쿼리를 사용하여 정보 조회
	@Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
	TodoEntity findByUserIdQuery(Long userId);

}
