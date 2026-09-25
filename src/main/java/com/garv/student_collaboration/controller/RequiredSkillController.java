package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.RequiredSkillRequest;
import com.garv.student_collaboration.dto.RequiredSkillResponse;
import com.garv.student_collaboration.service.RequiredSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Required Skills",
        description = "Manage the skills required for collaboration requests"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequiredSkillController {
    private final RequiredSkillService requiredSkillService;
    @Operation(
            summary = "Add a required skill",
            description = "Adds a skill requirement to a collaboration request owned by the authenticated student."
    )
    @PostMapping("/required-skills")
    @ResponseStatus(HttpStatus.CREATED)
    public RequiredSkillResponse createRequiredSkill(@Valid @RequestBody RequiredSkillRequest requiredSkillRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return requiredSkillService.createRequiredSkill(requiredSkillRequest,studentId);
    }
    @Operation(
            summary = "Get a required skill",
            description = "Returns one required-skill entry by its ID."
    )
    @GetMapping("/required-skills/{id}")
    public RequiredSkillResponse getRequiredSkillById(@PathVariable("id") Long id) {
        return requiredSkillService.getRequiredSkillById(id);
    }
    @Operation(
            summary = "List required skills",
            description = "Returns the skills required for a collaboration request."
    )
    @GetMapping("/collaboration-request/{collaborationRequestId}/required-skill")
    public List<RequiredSkillResponse> getRequiredSkillsByCollaborationRequestId(@PathVariable("collaborationRequestId") Long collaborationRequestId) {
        return requiredSkillService.getRequiredSkillsByCollaborationRequestId(collaborationRequestId);
    }
    @Operation(
            summary = "Remove a required skill",
            description = "Removes a skill requirement from a collaboration request owned by the authenticated student."
    )
    @DeleteMapping("/required-skills/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequiredSkillById(@PathVariable("id") Long id, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        requiredSkillService.deleteRequiredSkillById(id,studentId);
    }
    @Operation(
            summary = "Approve suggested skills",
            description = "Adds selected AI-generated skill suggestions to a collaboration request."
    )
    @PostMapping("/required-skills/approve")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RequiredSkillResponse> approveRequiredSkills(@RequestBody  List<@Valid RequiredSkillRequest> requiredSkillRequests, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return requiredSkillService.createRequiredSkills(requiredSkillRequests,studentId);
    }
}
