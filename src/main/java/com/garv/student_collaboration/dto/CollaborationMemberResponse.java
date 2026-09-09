package com.garv.student_collaboration.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationMemberResponse {
    private Long id;
    private Long collaborationRequestId;
    private Long studentId;
    private String studentName;
    private LocalDateTime joinedAt;
}
