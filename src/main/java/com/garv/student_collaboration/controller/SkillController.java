package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.SkillRequest;
import com.garv.student_collaboration.dto.SkillResponse;
import com.garv.student_collaboration.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;
    @PostMapping("/skills")
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(@Valid @RequestBody SkillRequest skillRequest) {
        return skillService.createSkill(skillRequest);
    }
    @GetMapping("/skills/{id}")
    public SkillResponse getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id);
    }
    @GetMapping("/skills")
    public List<SkillResponse> getSkills() {
        return skillService.getAllSkills();
    }
}
