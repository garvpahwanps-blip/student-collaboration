package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.JoinRequest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequestResponse {
    private Long id;
    private Long collaborationRequestId;
    private Long requestedById;
    private String requestedByName;
    private JoinRequest.Status status;
    private String message;
    private LocalDateTime createdAt;
}
