package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"collaboration_request_id","skill_id"})
})
@Entity
public class RequiredSkill {
    public enum DesiredLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
    public enum Importance{
        LOW,
        MEDIUM,
        HIGH
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaboration_request_id",nullable = false)
    private CollaborationRequest collaborationRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id",nullable = false)
    private Skill skill;
    @NotNull(message = "desired level is required")
    @Enumerated(EnumType.STRING)
    private DesiredLevel desiredLevel;
    @NotNull(message = "importance of skill is required")
    @Enumerated(EnumType.STRING)
    private Importance importance;
}
