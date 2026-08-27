package com.garv.student_collaboration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestRequest {
    @NotBlank(message = "Enter the interest name")
    private String name;
}
