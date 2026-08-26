package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CreateStudentRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse addStudent(@Valid @RequestBody CreateStudentRequest request) {
        return studentService.createStudent(request);
    }
    @GetMapping("/students/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    @GetMapping("/students")
    public List<StudentResponse> getStudents() {
        return studentService.getAllStudents();
    }
}
