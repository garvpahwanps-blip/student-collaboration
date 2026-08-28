package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.CollaborationRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationRequestRequest {
    @NotNull(message = "Id is required of the Student who created it")
    private Long createdById;
    @NotNull(message = "Goal id is required")
    private Long goalId;
    @NotBlank(message = "Title can not be blank")
    private String title;
    @NotBlank(message = "description can not be empty")
    private String description;
    @NotNull(message = "weeklyHours are needed")
    @Min(value = 1,message = "minimum weekly hours is 1")
    @Max(value = 45,message = "maximum weekly hours are 168")
    private Integer weeklyHours;
    @NotNull(message = "Please provide the collaboration mode")
    private CollaborationRequest.CollaborationMode collaborationMode;

}
