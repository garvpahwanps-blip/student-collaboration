package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.LoginRequest;
import com.garv.student_collaboration.dto.LoginResponse;
import com.garv.student_collaboration.dto.RegisterRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public StudentResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @GetMapping("/me")
    public StudentResponse getCurrentStudent(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return authService.getCurrentStudent(studentId);
    }
}
