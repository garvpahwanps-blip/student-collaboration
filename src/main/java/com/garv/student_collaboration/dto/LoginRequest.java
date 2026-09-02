package com.garv.student_collaboration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "email can not be null")
    @Email(message = "Please enter a valid email")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
