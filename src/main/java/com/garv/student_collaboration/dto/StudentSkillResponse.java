package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.StudentSkill;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSkillResponse {
    private Long id;
    private Long studentId;
    private Long skillId;
    private String skillName;
    private StudentSkill.Level level;
    private boolean learning;
}
