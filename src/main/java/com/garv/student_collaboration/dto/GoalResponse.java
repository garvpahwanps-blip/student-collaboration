package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Goal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {
    private Long id;
    private String description;
    private Goal.Priority priority;
    private Goal.Type type;
    private Goal.Status status;
    private Long studentId;

}
