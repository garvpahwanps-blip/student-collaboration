package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.LoginRequest;
import com.garv.student_collaboration.dto.LoginResponse;
import com.garv.student_collaboration.dto.RegisterRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.DuplicateEmailException;
import com.garv.student_collaboration.exception.InvalidCredentialException;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private StudentResponse toStudentResponse(Student student){
        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .gender(student.getGender())
                .birthDate(student.getBirthDate())
                .profilePicture(student.getProfilePicture())
                .university(student.getUniversity())
                .yearOfStudy(student.getYearOfStudy())
                .build();
    }
    public StudentResponse register(RegisterRequest registerRequest) {
        String normalizedEmail = registerRequest.getEmail().trim().toLowerCase(Locale.ROOT);
        if(studentRepository.existsByEmail(normalizedEmail)){
            throw new DuplicateEmailException("Email already exists: "+normalizedEmail);
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        StudentResponse savedStudent = toStudentResponse(studentRepository.save(Student.builder()
                .gender(registerRequest.getGender())
                .birthDate(registerRequest.getBirthDate())
                .email(normalizedEmail)
                .fullName(registerRequest.getFullName())
                .profilePicture(registerRequest.getProfilePicture())
                .university(registerRequest.getUniversity())
                .yearOfStudy(registerRequest.getYearOfStudy())
                .passwordHash(hashedPassword).build()));
        return savedStudent;
    }
    public LoginResponse login(LoginRequest loginRequest) {
        String normalizedEmail = loginRequest.getEmail().trim().toLowerCase(Locale.ROOT);
        Student student = studentRepository.findByEmail(normalizedEmail).orElseThrow(()->new InvalidCredentialException("Invalid email or password"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), student.getPasswordHash())){
            throw new InvalidCredentialException("Invalid email or password");
        }
        String accessToken = jwtService.generateToken(student.getId());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .student(toStudentResponse(student))
                .build();
    }
}
