package com.loginSample.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loginSample.todo.entity.Todo;
import com.loginSample.todo.entity.User;
import com.loginSample.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드 생성자
@Service // 서비스 계층(스프링이 관리)
public class TodoService {
	
	private final TodoRepository todoRepository; // 할일관리 DB
	
	// 신규 Todo 등록
	public Todo addTodo(Todo todo, User user) {
		todo.setUser(user);
		return todoRepository.save(todo);
	}
	
	
	// 사용자의 Todo 목록 조회
	public List<Todo> getTodoByUser(User user){
		return todoRepository.findByUserId(user.getId());
	}
	
	// Todo 삭제
	public void deleteTodoById(Long id, User user) {
		// ID로 할일 조회 -> 없으면 예외 처리
		Todo todo = this.findById(id, user);
		
		// Todo 삭제
		todoRepository.delete(todo);
	}

	// ID로 Todo조회
	public Optional<Todo> getTodoById(Long id){
		return todoRepository.findById(id);
	}
	
	// 특정 ID의 Todo 수정
	public void updateTodo(Long id, String title, String description, User user) {
		// ID로 할일 조회 -> 없으면 예외 처리
		Todo todo = this.findById(id, user);
		
		// 제목,설명 수정 
		todo.setTitle(title);
		todo.setDescription(description);
		
		// 저장
		todoRepository.save(todo);
		
	}
	
	// ID로 Todo 조회
	private Todo findById(Long id, User user) {
		Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할 일"));
		
		// 사용자ID와 할일의 사용자 ID비교
		if(!todo.getUser().getId().equals(user.getId())) {
			throw new SecurityException("권한없음");
		}
				
		return todo;
	}
}
