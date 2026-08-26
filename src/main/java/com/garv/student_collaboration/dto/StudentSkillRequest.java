package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.StudentSkill;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSkillRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    @NotNull(message = "Skill ID is required")
    private Long skillId;
    @NotNull(message = "Level is required")
    private StudentSkill.Level level;
    @NotNull(message = "Learning is required")
    private Boolean learning;
}
