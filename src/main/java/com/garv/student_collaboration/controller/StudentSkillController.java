package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentSkillRequest;
import com.garv.student_collaboration.dto.StudentSkillResponse;
import com.garv.student_collaboration.service.StudentSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Student Skills",
        description = "Manage the skills, proficiency levels, and learning status of the authenticated student"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentSkillController {
    private final StudentSkillService studentSkillService;
    @Operation(
            summary = "Add a student skill",
            description = "Adds a skill, proficiency level, and learning status for the authenticated student."
    )
    @PostMapping("/student-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentSkillResponse createStudentSkill(@Valid @RequestBody StudentSkillRequest studentSkillRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentSkillService.createStudentSkill(studentSkillRequest,studentId);
    }
    @Operation(
            summary = "List student skills",
            description = "Returns the skills recorded for a student."
    )
    @GetMapping("/student/{studentId}/skills")
    public List<StudentSkillResponse> getAllStudentSkillByStudentId(@PathVariable Long studentId) {
        return studentSkillService.getAllStudentSkills(studentId);
    }

}
