package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.RequiredSkill;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredSkillResponse {
    private Long id;
    private Long collaborationRequestId;
    private Long skillId;
    private String skillName;
    private RequiredSkill.DesiredLevel desiredLevel;
    private RequiredSkill.Importance importance;
}
