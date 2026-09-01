package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.RequiredSkillRequest;
import com.garv.student_collaboration.dto.RequiredSkillResponse;
import com.garv.student_collaboration.service.RequiredSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequiredSkillController {
    private final RequiredSkillService requiredSkillService;
    @PostMapping("/required-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public RequiredSkillResponse createRequiredSkill(@Valid @RequestBody RequiredSkillRequest requiredSkillRequest) {
        return requiredSkillService.createRequiredSkill(requiredSkillRequest);
    }
    @GetMapping("/required-skills/{id}")
    public RequiredSkillResponse getRequiredSkillById(@PathVariable("id") Long id) {
        return requiredSkillService.getRequiredSkillById(id);
    }
    @GetMapping("/collaboration-request/{collaborationRequestId}/required-skill")
    public List<RequiredSkillResponse> getRequiredSkillsByCollaborationRequestId(@PathVariable("collaborationRequestId") Long collaborationRequestId) {
        return requiredSkillService.getRequiredSkillsByCollaborationRequestId(collaborationRequestId);
    }
    @DeleteMapping("/required-skills/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequiredSkillById(@PathVariable("id") Long id) {
        requiredSkillService.deleteRequiredSkillById(id);
    }
    @PostMapping("/required-skills/approve")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RequiredSkillResponse> approveRequiredSkills(@RequestBody @Valid List<RequiredSkillRequest> requiredSkillRequests) {
        return requiredSkillService.createRequiredSkills(requiredSkillRequests);
    }
}
