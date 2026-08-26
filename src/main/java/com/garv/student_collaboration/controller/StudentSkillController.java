package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.StudentSkillRequest;
import com.garv.student_collaboration.dto.StudentSkillResponse;
import com.garv.student_collaboration.service.StudentSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentSkillController {
    private final StudentSkillService studentSkillService;
    @PostMapping("/student-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentSkillResponse createStudentSkill(@Valid @RequestBody StudentSkillRequest studentSkillRequest) {
        return studentSkillService.createStudentSkill(studentSkillRequest);
    }

}
