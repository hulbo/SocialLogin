package com.loginSample.todo.controller;

import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loginSample.todo.entity.User;
import com.loginSample.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

/*
 * 사용자 등록 컨트롤 
 */

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 등록페이지 이동
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    // 사용자 등록
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
    	
    	log.info("### 등록사용자 ID :: " + user.getUsername());
    	
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailPattern, user.getUsername())) {
            result.rejectValue("username", "error.user", "Invalid email format. Please enter a valid email address.");
            model.addAttribute("emailError", "Invalid email format. Please enter a valid email address.");
        }

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        userService.registerUser(user.getUsername(), user.getPassword());
        return "redirect:/login";
    }

}