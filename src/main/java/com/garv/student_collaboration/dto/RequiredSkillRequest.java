package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.RequiredSkill;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredSkillRequest {
    @NotNull(message = "Collaboration Request id is required")
    private Long collaborationRequestId;
    @NotNull(message = "Skill id is required")
    private Long skillId;
    @NotNull(message = "Desired level of the skill is required")
    private RequiredSkill.DesiredLevel desiredLevel;
    @NotNull(message = "Importance of the skill is required")
    private RequiredSkill.Importance importance;
}
