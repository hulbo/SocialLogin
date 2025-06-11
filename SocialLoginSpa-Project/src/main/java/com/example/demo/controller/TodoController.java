package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.entity.TodoEntity;
import com.example.demo.service.TodoService;

import lombok.RequiredArgsConstructor;

/**
 * Todo 리스트 관리 API 컨트롤러
 * - @RestController: RESTful 웹 서비스 컨트롤러임을 명시
 * - @RequestMapping("todo"): 기본 URL을 `/todo`로 설정
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("todo")
public class TodoController {

    private final TodoService service; // Todo 관련 비즈니스 로직을 처리하는 서비스

    /**
     * Todo 생성 API
     * - @PostMapping: HTTP POST 요청을 처리
     * - @AuthenticationPrincipal: 현재 인증된 사용자의 ID를 가져옴
     * - @RequestBody: JSON 형태의 요청 데이터를 자동으로 DTO로 변환
     */
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // DTO를 Entity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setId(null); // 새 Todo이므로 ID는 null
            entity.setUserId(Long.parseLong(userId)); // 현재 로그인한 사용자 ID 설정

            // 서비스 호출하여 Todo 생성
            List<TodoEntity> entities = service.create(entity);

            // 결과를 DTO로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 응답 객체 생성
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Todo 리스트 조회 API
     * - @GetMapping: HTTP GET 요청을 처리
     */
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        // 현재 로그인한 사용자의 Todo 리스트 가져오기
        List<TodoEntity> entities = service.retrieve(Long.parseLong(userId));

        // Entity → DTO 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 응답 객체 생성 및 반환
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * Todo 수정 API
     * - @PutMapping: HTTP PUT 요청을 처리 (기존 데이터 수정)
     */
    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(Long.parseLong(userId)); // 사용자 ID 설정

        // 서비스 호출하여 수정된 Todo 리스트 가져오기
        List<TodoEntity> entities = service.update(entity);

        // 결과를 DTO로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * Todo 삭제 API
     * - @DeleteMapping: HTTP DELETE 요청을 처리
     */
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setUserId(Long.parseLong(userId));

            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}