package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.CollaborationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollaborationMemberRepository extends JpaRepository<CollaborationMember, Long> {
    boolean existsByCollaborationRequest_IdAndStudent_Id(Long collaborationRequestId, Long studentId);
    List<CollaborationMember> findAllByCollaborationRequest_Id(Long collaborationRequestId);
    Optional<CollaborationMember> findByCollaborationRequest_IdAndStudent_Id(Long collaborationRequestId, Long studentId);
}
