package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.CollaborationRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCollaborationRequestRequest {
    private String title;
    private String description;
    @Min(1)
    @Max(45)
    private Integer weeklyHours;
    private CollaborationRequest.CollaborationMode collaborationMode;
}
