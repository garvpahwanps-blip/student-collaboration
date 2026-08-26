package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Skill;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private Skill.Category category;
}
