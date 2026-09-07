package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.CollaborationMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaborationMemberRepository extends JpaRepository<CollaborationMember, Long> {
    boolean existsByCollaborationRequest_IdAndStudent_Id(Long collaborationRequestId, Long studentId);
}
