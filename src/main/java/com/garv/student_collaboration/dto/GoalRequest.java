package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Goal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalRequest {
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "priority is required")
    private Goal.Priority priority;
    @NotNull(message = "type is required")
    private Goal.Type type;
    @NotNull(message = "Student id is required")
    private Long studentId;
}
