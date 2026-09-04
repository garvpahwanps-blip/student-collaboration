package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.RequiredSkillRequest;
import com.garv.student_collaboration.dto.RequiredSkillResponse;
import com.garv.student_collaboration.service.RequiredSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequiredSkillController {
    private final RequiredSkillService requiredSkillService;
    @PostMapping("/required-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public RequiredSkillResponse createRequiredSkill(@Valid @RequestBody RequiredSkillRequest requiredSkillRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return requiredSkillService.createRequiredSkill(requiredSkillRequest,studentId);
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
    public void deleteRequiredSkillById(@PathVariable("id") Long id, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        requiredSkillService.deleteRequiredSkillById(id,studentId);
    }
    @PostMapping("/required-skills/approve")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RequiredSkillResponse> approveRequiredSkills(@RequestBody  List<@Valid RequiredSkillRequest> requiredSkillRequests, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return requiredSkillService.createRequiredSkills(requiredSkillRequests,studentId);
    }
}
