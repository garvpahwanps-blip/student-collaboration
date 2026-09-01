package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "name can not be blank")
    private String fullName;
    @NotBlank(message = "email can not be blank")
    @Email(message = "please enter a valid email")
    private String email;
    private String university;
    private LocalDate birthDate;
    private Student.Gender gender;
    private String profilePicture;
    @NotNull(message = "year of Study is required")
    private Student.YearOfStudy yearOfStudy;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

}
