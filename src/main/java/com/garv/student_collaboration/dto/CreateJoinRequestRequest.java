package com.garv.student_collaboration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJoinRequestRequest {
    @NotNull(message = "Collaboration request ID is required")
    private Long collaborationRequestId;
    private String message;

}
