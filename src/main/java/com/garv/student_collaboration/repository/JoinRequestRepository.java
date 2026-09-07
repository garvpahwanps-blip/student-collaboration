package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinRequestRepository  extends JpaRepository<JoinRequest, Long> {
    boolean existsByCollaborationRequest_IdAndRequestedBy_Id(Long collaborationRequestId, Long requestedById);
    Optional<JoinRequest> findAllByRequestedBy_Id(Long studentid);
    Optional<JoinRequest> findAllByCollaborationRequest_CreatedBy_Id(Long studentId);
}
