package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentInterestRequest;
import com.garv.student_collaboration.dto.StudentInterestResponse;
import com.garv.student_collaboration.service.StudentInterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Student Interests",
        description = "Manage interests on the authenticated student's profile"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentInterestController {
    private final StudentInterestService studentInterestService;
    @Operation(
            summary = "Add a student interest",
            description = "Adds an interest to the authenticated student's profile."
    )
    @PostMapping("/student-interests")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentInterestResponse createStudentInterest(@RequestBody @Valid StudentInterestRequest studentInterestRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentInterestService.createStudentInterest(studentInterestRequest,studentId);
    }
    @Operation(
            summary = "List student interests",
            description = "Returns interests recorded for a student."
    )
    @GetMapping("/student/{studentId}/interests")
    public List<StudentInterestResponse> getAllStudentInterestByStudentId(@PathVariable Long studentId){
        return studentInterestService.getAllInterestByStudentId(studentId);
    }

}
