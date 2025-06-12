package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);

	@Query("SELECT COUNT(t.id) > 0 FROM UserEntity t WHERE t.username = ?1")
	Boolean existsByUsername(String username);

	UserEntity findByUsernameAndPassword(String username, String password);
}
