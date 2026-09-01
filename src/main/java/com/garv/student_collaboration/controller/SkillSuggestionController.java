package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.SkillSuggestionResponse;
import com.garv.student_collaboration.service.SkillSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collaboration-requests")
@RequiredArgsConstructor
public class SkillSuggestionController {
    private final SkillSuggestionService skillSuggestionService;
    @PostMapping("/{collaborationRequestId}/skill-suggestions")
    public List<SkillSuggestionResponse> getSuggestedSkillsForRequest(@PathVariable Long collaborationRequestId) {
        return skillSuggestionService.suggestSkillsForRequest(collaborationRequestId);
    }
}
