package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id","skill_id"})}
)
@Entity
public class StudentSkill {
    public enum Level{
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    @NotNull(message = "Level is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
    private boolean learning;

}
