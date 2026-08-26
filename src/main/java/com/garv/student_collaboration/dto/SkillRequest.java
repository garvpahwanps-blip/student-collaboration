package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillRequest {
    @NotBlank(message = "Enter the skill name")
    private String name;
    @NotNull(message = "Category is required")
    private Skill.Category category;
}
