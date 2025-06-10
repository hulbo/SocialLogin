package com.loginSample.todo.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity // JPA에서 이클레스 엔터티로 지정
@Table(name = "t_sl_users") // 테이블명
public class User {
	
	@Id // 기본키지정
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_sl_users")
	@SequenceGenerator(name = "s_sl_users", sequenceName = "s_sl_users", allocationSize = 1)
	private Long id; // 유 ID
	
	private String username; // 사용자 이름
	private String password; // 사용자 비밀번호
	
	private String socialType; // 소셜 로그인 유형(예:google, kakao 등)
	private String socialId; // 소셜 로그인 ID(외부에서 제공받은 정보)
	
	// 사용자와 할 일 간의 1:N 관계매핑
	@OneToMany(mappedBy = "user")
	private Set<Todo> todos;
	
}
