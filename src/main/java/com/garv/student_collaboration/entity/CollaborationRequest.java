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
@Table(name = "collaboration_requests")
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
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Title can not be blank")
    private String title;
    @Column(nullable = false, length = 1000)
    @NotBlank(message = "description can not be empty")
    private String description;
    @NotNull(message = "weeklyHours are needed")
    @Min(1)
    @Max(45)
    private Integer weeklyHours;
    @NotNull(message = "Please provide the collaboration mode")
    @Enumerated(EnumType.STRING)
    private CollaborationMode collaborationMode;
    @Column(nullable = false)
    @NotNull(message = "Please provide status")
    @Enumerated(EnumType.STRING)
    private Status status;



}
