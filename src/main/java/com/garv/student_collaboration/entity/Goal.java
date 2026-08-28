package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "goals")
public class Goal {
    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
    public enum Type{
        PROJECT,HACKATHON,GROUP_STUDY,SKILL_MENTOR
    }
    public enum Priority{
        LOW,MEDIUM,HIGH
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 250)
    private String description;
    @NotNull(message = "priority is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @NotNull(message = "type is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @NotNull(message = "Status is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="student_id",nullable = false)
    private Student student;

}
