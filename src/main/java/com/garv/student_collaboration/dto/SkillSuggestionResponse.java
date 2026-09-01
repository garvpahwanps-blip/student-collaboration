package com.garv.student_collaboration.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSuggestionResponse {
    private Long skillId;
    private String skillName;
    private String desiredLevel;
    private String importance;
    private String reason;
}
