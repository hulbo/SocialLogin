package com.loginSample.todo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity // JPA에서 이클레스 엔터티로 지정
@Table(name = "t_sl_todos") // 테이블명
public class Todo {
	
	@Id // 기본키지정
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_sl_todo")
	@SequenceGenerator(name = "s_sl_todo", sequenceName = "s_sl_todo", allocationSize = 1)
	private Long id; // 할일고유 ID
	
	private String title; // 할 일 제목
	private String description; // 할 일 설명
	private boolean completed; // 완료여부
	
	// 다대일(N:1) 관계 맵핑
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;	
}
