package com.garv.student_collaboration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJoinRequestStatusRequest {

    public enum Decision {
        ACCEPTED,
        REJECTED
    }

    @NotNull(message = "Decision is required")
    private Decision decision;
}
