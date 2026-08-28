package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.CollaborationRequest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationRequestResponse {
    private Long id;
    private Long createdById;
    private Long goalId;
    private String title;
    private String description;
    private Integer weeklyHours;
    private CollaborationRequest.CollaborationMode collaborationMode;
    private CollaborationRequest.Status status;

}
