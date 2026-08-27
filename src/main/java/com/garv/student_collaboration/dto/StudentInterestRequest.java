package com.garv.student_collaboration.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentInterestRequest {
    @NotNull(message = "Student id is required")
    private Long studentId;
    @NotNull(message = "Interest id is required")
    private Long interestId;
}
