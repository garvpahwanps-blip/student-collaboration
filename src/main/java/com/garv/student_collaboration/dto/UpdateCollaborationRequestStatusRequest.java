package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.CollaborationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCollaborationRequestStatusRequest {
    @NotNull(message = "Status is required")
    private CollaborationRequest.Status status;
}
