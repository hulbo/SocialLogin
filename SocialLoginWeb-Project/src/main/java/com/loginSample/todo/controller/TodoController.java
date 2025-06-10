package com.loginSample.todo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.loginSample.todo.entity.Todo;
import com.loginSample.todo.entity.User;
import com.loginSample.todo.security.vo.CustomUserDetails;
import com.loginSample.todo.service.TodoService;
import com.loginSample.todo.service.UserService;

import lombok.RequiredArgsConstructor;


/**
 * 할일 관리 컨트롤러
 * */

@RequiredArgsConstructor
@Controller
@RequestMapping("/todos")
public class TodoController {
    
	// 할일 관리 서비스
	private final TodoService todoService;
	// 사용자 관리 서비스
    private final UserService userService;

    /*
    좀더 간결 한 소스
    @AuthenticationPrincipal 를 사용하면 변환과정없이 customUser.getUsername() 사용이 가능하다.
    @GetMapping
    public String sample(@AuthenticationPrincipal CustomUserDetails customUser, Model model) {
    	System.err.println(customUser.getUsername());
    	return "";
    }
    */
    
    // 사용자의 할 일 목록을 조회하여 뷰에 전달
    @GetMapping
    public String listTodos(Authentication authentication, Model model) {
    	Object principal = authentication.getPrincipal();
    	
    	if (principal == null) {
            return "redirect:/login";
        }
    	
    	CustomUserDetails customUser = (CustomUserDetails) principal;
    	
        Optional<User> user = userService.findByUsername(customUser.getUsername());
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        
        List<Todo> todos = todoService.getTodoByUser(user.get());
        model.addAttribute("todos", todos);
        
        return "todos";
    }

    // 할일 등록
    @PostMapping("/add")
    public String addTodo(Authentication authentication, @ModelAttribute Todo todo) {
    	Object principal = authentication.getPrincipal();
    	
    	if (principal == null) {
            return "redirect:/login";
        }
    	
    	CustomUserDetails customUser = (CustomUserDetails) principal;
    	
    	User user = new User();
    	user.setId(customUser.getUserid());
    	
        todoService.addTodo(todo, user);
        return "redirect:/todos";
    }

    // 할일 삭제
    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id, Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	
    	if (principal == null) {
            return "redirect:/login";
        }
    	
    	CustomUserDetails customUser = (CustomUserDetails) principal;
    	
    	User user = new User();
    	user.setId(customUser.getUserid());
    	
        todoService.deleteTodoById(id, user);
        return "redirect:/todos";
    }
    
    // 할일 수정 이동
    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable("id") Long id, Model model, Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	
    	if (principal == null) {
            return "redirect:/login";
        }
    	
    	CustomUserDetails customUser = (CustomUserDetails) principal;
    	
    	Long userId = customUser.getUserid();
    	
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent() && todo.get().getUser().getId().equals(userId)) {
            model.addAttribute("todo", todo.get());
            return "edit_todo";
        }
        return "redirect:/todos";
    }

    // 할일 수정
    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable("id") Long id, @RequestParam("title") String title, 
    		@RequestParam("description") String description, Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	
    	if (principal == null) {
            return "redirect:/login";
        }
    	
    	CustomUserDetails customUser = (CustomUserDetails) principal;
    	
    	User user = new User();
    	user.setId(customUser.getUserid());
    	
    	todoService.updateTodo(id, title, description, user);
        return "redirect:/todos";
    }
    
}