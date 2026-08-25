package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.CollaborationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest,Long> {
    List<CollaborationRequest> findAllByCreatedBy_Id(Long studentId);
    List<CollaborationRequest> findAllByStatus(CollaborationRequest.Status status);
}
