package com.example.demo.dto;

import com.example.demo.entity.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 클라이언트 데이터 전송을 위한 클래스
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
	
	private Long id; // 할일ID
	private String title; // 할일
	private boolean done; // 완료여부

	// 엔터티를 -> DTO 로 변환
	public TodoDTO(final TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}

	// DTO -> 엔터티 로 변환
	public static TodoEntity toEntity(final TodoDTO dto) {
		return TodoEntity.builder()
			.id(dto.getId())
			.title(dto.getTitle())
			.done(dto.isDone())
			.build();
	}

}
