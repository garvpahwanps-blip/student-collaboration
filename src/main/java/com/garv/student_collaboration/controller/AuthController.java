package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.RegisterRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
