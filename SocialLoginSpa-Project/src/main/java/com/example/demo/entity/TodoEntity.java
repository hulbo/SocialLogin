package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_sl_todo 테이블 매핑 클래스
 */

@Builder // 빌더 패턴사용
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든필드 생성자
@Data
@Entity
@Table(name = "t_sl_todo")
public class TodoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_sl_todo")
	@SequenceGenerator(name = "s_sl_todo", sequenceName = "s_sl_todo", allocationSize = 1)
	private Long id; // 고유ID
	
	private Long userId; // 사용자ID
	
	private String title; // 할일내용
	
	private boolean done; // 완료여부
	
}
