package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 🔐 사용자 인증 컨트롤러
 * - @RestController: RESTful API 컨트롤러
 * - @RequestMapping("/auth"): 기본 URL을 `/auth`로 설정
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService; // 사용자 관련 비즈니스 로직 처리
    private final TokenProvider tokenProvider; // JWT 토큰 생성 및 관리
    
    // 🔑 비밀번호 암호화 처리 (BCrypt 알고리즘 사용)
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 회원가입 API
     * - @PostMapping("/signup"): HTTP POST 요청을 처리
     * - @RequestBody: JSON 형태의 요청 데이터를 UserDTO로 변환
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 1️⃣ 요청 값 검증: userDTO 또는 비밀번호가 null이면 예외 발생
            if (userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value.");
            }

            // 2️⃣ 사용자 정보 생성 및 비밀번호 암호화
            UserEntity user = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword())) // 비밀번호 암호화
                .build();

            // 3️⃣ DB에 사용자 정보 저장
            UserEntity registeredUser = userService.create(user);

            // 4️⃣ 응답 객체 생성 (보안상 비밀번호는 포함하지 않음)
            UserDTO responseUserDTO = UserDTO.builder()
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .build();

            return ResponseEntity.ok().body(responseUserDTO); // 회원가입 성공 시 200 OK 반환
        } 
        catch (Exception e) {
            // 5️⃣ 예외 발생 시 오류 메시지 반환
            ResponseDTO responseDTO = ResponseDTO.builder()
                .error(e.getMessage())
                .build();

            return ResponseEntity.badRequest().body(responseDTO); // 400 Bad Request 반환
        }
    }

    /**
     * 로그인 API
     * - @PostMapping("/signin"): HTTP POST 요청을 처리
     * - @RequestBody: JSON 형태의 요청 데이터를 UserDTO로 변환
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        // 1️⃣ 사용자 인증 (DB에서 조회)
        UserEntity user = userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword(), passwordEncoder);

        if (user != null) {
            // 2️⃣ 인증 성공 시 JWT 토큰 생성
            final String token = tokenProvider.create(user);

            // 3️⃣ 응답 객체 생성 (JWT 포함)
            final UserDTO responseUserDTO = UserDTO.builder()
                .username(user.getUsername())
                .id(user.getId())
                .token(token) // 로그인 성공 시 JWT 토큰 제공
                .build();

            return ResponseEntity.ok().body(responseUserDTO); // 로그인 성공 시 200 OK 반환
        } 
        else {
            // 4️⃣ 로그인 실패 시 오류 메시지 반환
            ResponseDTO responseDTO = ResponseDTO.builder()
                .error("Login failed.")
                .build();

            return ResponseEntity.badRequest().body(responseDTO); // 400 Bad Request 반환
        }
    }

}