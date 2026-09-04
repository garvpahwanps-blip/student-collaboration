package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.*;
import com.garv.student_collaboration.service.AuthService;
import com.garv.student_collaboration.service.StudentService;
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
    private final StudentService studentService;
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
    @PatchMapping("/me")
    public StudentResponse updateCurrentStudent( @Valid @RequestBody UpdateStudentRequest updateStudentRequest,Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.updateStudent(studentId,updateStudentRequest);
    }
}
