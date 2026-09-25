package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.SkillSuggestionResponse;
import com.garv.student_collaboration.service.SkillSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(
        name = "AI Skill Suggestions",
        description = "Generate AI-suggested skills for collaboration requests"
)
@RestController
@RequestMapping("/api/collaboration-requests")
@RequiredArgsConstructor
public class SkillSuggestionController {
    private final SkillSuggestionService skillSuggestionService;
    @Operation(
            summary = "Generate required-skill suggestions",
            description = "Uses AI to suggest skills that may be required for a collaboration request."
    )
    @PostMapping("/{collaborationRequestId}/skill-suggestions")
    public List<SkillSuggestionResponse> getSuggestedSkillsForRequest(@PathVariable Long collaborationRequestId) {
        return skillSuggestionService.suggestSkillsForRequest(collaborationRequestId);
    }
}
