package com.garv.student_collaboration.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentInterestResponse {
    private Long id;
    private Long studentId;
    private Long interestId;
    private String interestName;
}
