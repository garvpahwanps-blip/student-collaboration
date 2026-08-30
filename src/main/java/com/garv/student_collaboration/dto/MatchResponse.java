package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Skill;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponse {
    private Long collaborationRequestId;
    private String title;
    private double score;
    private List<String> matchingSkills;
    private List<String> missingSkills;

}
