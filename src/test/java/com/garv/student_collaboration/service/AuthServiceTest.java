package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.LoginRequest;
import com.garv.student_collaboration.dto.LoginResponse;
import com.garv.student_collaboration.exception.DuplicateEmailException;
import com.garv.student_collaboration.exception.InvalidCredentialException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.garv.student_collaboration.dto.RegisterRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.entity.Student;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;
    @Test
    void register_shouldNormalizeEmailEncodePasswordAndReturnStudentResponse(){
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Garv Pahwa")
                .email("  GARV@EXAMPLE.COM  ")
                .university("Example University")
                .password("password123")
                .build();
        when(studentRepository.existsByEmail("garv@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student studentToSave = invocation.getArgument(0);
            studentToSave.setId(100L);
            return studentToSave;
        });
        StudentResponse response = authService.register(request);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student savedStudent = studentArgumentCaptor.getValue();
        assertEquals("Garv Pahwa", savedStudent.getFullName());
        assertEquals("garv@example.com", savedStudent.getEmail());
        assertEquals("hashed-password", savedStudent.getPasswordHash());

        assertEquals(100L, response.getId());
        assertEquals("Garv Pahwa", response.getFullName());
        assertEquals("garv@example.com", response.getEmail());
    }
    @Test
    void register_shouldThrowExceptionWhenEmailAlreadyExists(){
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Garv Pahwa")
                .email("  GARV@EXAMPLE.COM  ")
                .university("Example University")
                .password("password123")
                .build();
        when(studentRepository.existsByEmail("garv@example.com")).thenReturn(true);
        assertThrows(DuplicateEmailException.class,()-> authService.register(request));
        verifyNoInteractions(passwordEncoder);
        verify(studentRepository, never()).save(any(Student.class));
    }
    @Test
    void Login_shouldReturnTokenAndStudentWhenCredentialsAreValid(){
        LoginRequest request = LoginRequest.builder()
                .email("  GARV@EXAMPLE.COM  ")
                .password("password123")
                .build();
        Student student = Student.builder()
                .id(100L)
                .fullName("Garv Pahwa")
                .email("garv@example.com")
                .passwordHash("hashed-password")
                .build();
        when(studentRepository.findByEmail("garv@example.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123","hashed-password")).thenReturn(true);
        when(jwtService.generateToken(100L)).thenReturn("fake-jwt-token");
        LoginResponse response = authService.login(request);
        assertEquals("fake-jwt-token", response.getAccessToken());
        assertEquals(100L, response.getStudent().getId());
        assertEquals("garv@example.com", response.getStudent().getEmail());
        verify(jwtService).generateToken(100L);
    }
    @Test
    void Login_shouldThrowExceptionWhenEmailDoesNotExist(){
        LoginRequest request = LoginRequest.builder()
                .email("  GARV@EXAMPLE.COM  ")
                .password("password123")
                .build();
        Student student = Student.builder()
                .id(100L)
                .fullName("Garv Pahwa")
                .email("garv@example.com")
                .passwordHash("hashed-password")
                .build();
        when(studentRepository.findByEmail("garv@example.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialException.class,()-> authService.login(request));
        verifyNoInteractions(passwordEncoder,jwtService);
    }
    @Test
    void Login_shouldThrowExceptionWhenPasswordDoesNotMatch(){
        LoginRequest request = LoginRequest.builder()
                .email("  GARV@EXAMPLE.COM  ")
                .password("password123")
                .build();
        Student student = Student.builder()
                .id(100L)
                .fullName("Garv Pahwa")
                .email("garv@example.com")
                .passwordHash("hashed-password")
                .build();
        when(studentRepository.findByEmail("garv@example.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123","hashed-password")).thenReturn(false);
        assertThrows(InvalidCredentialException.class,()-> authService.login(request));
        verifyNoInteractions(jwtService);

    }
    @Test
    void getCurrentStudent_shouldReturnCurrentStudent(){
        Student student = Student.builder()
                .id(100L)
                .fullName("Garv Pahwa")
                .email("garv@example.com")
                .passwordHash("hashed-password")
                .build();
        when(studentRepository.findById(100L)).thenReturn(Optional.of(student));
        StudentResponse response = authService.getCurrentStudent(student.getId());
        assertEquals(100L, response.getId());
        assertEquals("garv@example.com", response.getEmail());
        verifyNoInteractions(jwtService,passwordEncoder);
    }
    @Test
    void getCurrentStudent_shouldThrowExceptionWhenStudentDoesNotExist(){
        when(studentRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class,()-> authService.getCurrentStudent(100L));
        verifyNoInteractions(passwordEncoder,jwtService);
    }
}
