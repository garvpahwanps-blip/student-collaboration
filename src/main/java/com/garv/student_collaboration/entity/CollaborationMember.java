package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "collaboration_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"collaboration_request_id", "student_id"}))
public class CollaborationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaboration_request_id", nullable = false)
    private CollaborationRequest collaborationRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private  Student student;
    @Column(nullable = false)
    private LocalDateTime joinedAt;
    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }
}
