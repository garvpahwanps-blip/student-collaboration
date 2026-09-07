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
@Table(name = "join_requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"collaboration_request_id", "student_id"}))
public class JoinRequest {
    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaboration_request_id", nullable = false)
    private CollaborationRequest collaborationRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student requestedBy;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    private String message;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void setDefaults(){
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }


}
