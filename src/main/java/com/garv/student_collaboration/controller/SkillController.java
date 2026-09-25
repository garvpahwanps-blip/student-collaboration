package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.SkillRequest;
import com.garv.student_collaboration.dto.SkillResponse;
import com.garv.student_collaboration.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Skills",
        description = "Browse and manage the available skill catalogue"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;
    @Operation(
            summary = "Create a skill",
            description = "Adds a skill to the platform skill catalogue."
    )
    @PostMapping("/skills")
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(@Valid @RequestBody SkillRequest skillRequest) {
        return skillService.createSkill(skillRequest);
    }
    @Operation(
            summary = "Get a skill",
            description = "Returns one skill by its ID."
    )
    @GetMapping("/skills/{id}")
    public SkillResponse getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id);
    }
    @Operation(
            summary = "List skills",
            description = "Returns all skills available on the platform."
    )
    @GetMapping("/skills")
    public List<SkillResponse> getSkills() {
        return skillService.getAllSkills();
    }
}
