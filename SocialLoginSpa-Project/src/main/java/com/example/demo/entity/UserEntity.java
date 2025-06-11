package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sl_user", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_sl_user")
	@SequenceGenerator(name = "s_sl_user", sequenceName = "s_sl_user", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String username; // 사용자명(이메일)

	private String password; // 비밀번호(소셜사용자는 사용안함)

	private String role; // 권한

	private String authProvider;
	
}
