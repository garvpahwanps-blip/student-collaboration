package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentSkillRequest;
import com.garv.student_collaboration.dto.StudentSkillResponse;
import com.garv.student_collaboration.service.StudentSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentSkillController {
    private final StudentSkillService studentSkillService;
    @PostMapping("/student-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentSkillResponse createStudentSkill(@Valid @RequestBody StudentSkillRequest studentSkillRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentSkillService.createStudentSkill(studentSkillRequest,studentId);
    }
    @GetMapping("/student/{studentId}/skills")
    public List<StudentSkillResponse> getAllStudentSkillByStudentId(@PathVariable Long studentId) {
        return studentSkillService.getAllStudentSkills(studentId);
    }

}
