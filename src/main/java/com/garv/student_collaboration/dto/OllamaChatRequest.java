package com.garv.student_collaboration.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OllamaChatRequest {
    private String model;
    private List<OllamaMessage> messages;
    private Boolean stream;
}
