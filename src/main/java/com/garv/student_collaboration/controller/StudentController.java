package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Students",
        description = "View student profiles"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @Operation(
            summary = "Get a student profile",
            description = "Returns one student profile by its ID."
    )
    @GetMapping("/students/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    @Operation(
            summary = "List students",
            description = "Returns student profiles available on the platform."
    )
    @GetMapping("/students")
    public List<StudentResponse> getStudents() {
        return studentService.getAllStudents();
    }

}
