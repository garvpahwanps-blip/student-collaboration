package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @GetMapping("/students/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    @GetMapping("/students")
    public List<StudentResponse> getStudents() {
        return studentService.getAllStudents();
    }

}
