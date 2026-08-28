package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Goal;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGoalStatusRequest {
    @NotNull(message = "Status is required")
    private Goal.Status status;
}
