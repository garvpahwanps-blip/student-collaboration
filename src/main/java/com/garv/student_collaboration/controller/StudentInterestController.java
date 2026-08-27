package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentInterestRequest;
import com.garv.student_collaboration.dto.StudentInterestResponse;
import com.garv.student_collaboration.service.StudentInterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentInterestController {
    private final StudentInterestService studentInterestService;
    @PostMapping("/student-interests")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentInterestResponse createStudentInterest(@RequestBody @Valid StudentInterestRequest studentInterestRequest){
        return studentInterestService.createStudentInterest(studentInterestRequest);
    }
    @GetMapping("/student/{studentId}/interests")
    public List<StudentInterestResponse> getAllStudentInterestByStudentId(@PathVariable Long studentId){
        return studentInterestService.getAllInterestByStudentId(studentId);
    }

}
