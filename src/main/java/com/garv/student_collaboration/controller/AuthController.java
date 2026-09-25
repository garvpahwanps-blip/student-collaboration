package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.*;
import com.garv.student_collaboration.service.AuthService;
import com.garv.student_collaboration.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Authentication",
        description = "Student registration, login, and current-profile operations"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final StudentService studentService;
    @Operation(
            summary = "Register a student account",
            description = "Creates a new student account with an encrypted password."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public StudentResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @Operation(
            summary = "Log in",
            description = "Verifies email and password, then returns a JWT access token."
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @Operation(
            summary = "Get current student",
            description = "Returns the profile of the student identified by the JWT token."
    )
    @GetMapping("/me")
    public StudentResponse getCurrentStudent(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return authService.getCurrentStudent(studentId);
    }
    @Operation(
            summary = "Update current student profile",
            description = "Updates the authenticated student's profile fields."
    )
    @PatchMapping("/me")
    public StudentResponse updateCurrentStudent( @Valid @RequestBody UpdateStudentRequest updateStudentRequest,Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.updateStudent(studentId,updateStudentRequest);
    }
}
