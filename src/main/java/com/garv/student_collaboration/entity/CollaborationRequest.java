package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CollaborationRequest {
    public enum CollaborationMode{
        ONLINE,
        OFFLINE,
        BOTH
    }
    public enum Status{
        OPEN,
        CLOSED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",nullable = false)
    private Student createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "goal_id",nullable = false)
    private Goal goal;
    @NotBlank(message = "Title can not be blank")
    private String title;
    @NotBlank(message = "description can not be empty")
    private String description;
    @NotNull(message = "weeklyHours are needed")
    @Min(0)
    @Max(45)
    private Integer weeklyHours;
    @NotNull(message = "Please provide the collaboration mode")
    @Enumerated(EnumType.STRING)
    private CollaborationMode collaborationMode;
    @NotNull(message = "Please provide status ")
    @Enumerated(EnumType.STRING)
    private Status status;



}
