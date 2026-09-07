package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.CreateJoinRequestRequest;
import com.garv.student_collaboration.dto.JoinRequestResponse;
import com.garv.student_collaboration.dto.UpdateJoinRequestStatusRequest;
import com.garv.student_collaboration.entity.CollaborationMember;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.JoinRequest;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.*;
import com.garv.student_collaboration.repository.CollaborationMemberRepository;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.JoinRequestRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final StudentRepository studentRepository;
    private  final CollaborationMemberRepository collaborationMemberRepository;
    public JoinRequestResponse createJoinRequest(CreateJoinRequestRequest request,Long studentId){
        CollaborationRequest collaborationRequest = collaborationRequestRepository.findById(request.getCollaborationRequestId()).orElseThrow(()->new CollaborationRequestNotFoundException("CollaborationRequest Not Found"));
        Student student = studentRepository.findById(studentId).orElseThrow(()->new StudentNotFoundException("Student Not Found"));
        if(!collaborationRequest.getStatus().equals(CollaborationRequest.Status.OPEN)){
            throw new RequestRejectedException("CollaborationRequest Not Open");
        }
        if(studentId.equals(collaborationRequest.getCreatedBy().getId())){
            throw new RequestRejectedException("Can not send request to your own request");
        }
        if(joinRequestRepository.existsByCollaborationRequest_IdAndRequestedBy_Id(collaborationRequest.getId(),studentId)){
            throw new RequestRejectedException("Request already exists");
        }
        JoinRequest savedJoinRequest = joinRequestRepository.save(JoinRequest.builder()
                .collaborationRequest(collaborationRequest)
                .requestedBy(student)
                .message(request.getMessage()).build());
        return JoinRequestResponse.builder()
                .id(savedJoinRequest.getId())
                .collaborationRequestId(savedJoinRequest.getCollaborationRequest().getId())
                .requestedById(savedJoinRequest.getRequestedBy().getId())
                .message(savedJoinRequest.getMessage())
                .requestedByName(savedJoinRequest.getRequestedBy().getFullName())
                .createdAt(savedJoinRequest.getCreatedAt())
                .status(savedJoinRequest.getStatus())
                .build();

    }
    @Transactional
    public JoinRequestResponse updateJoinRequestStatus(Long joinRequestId, UpdateJoinRequestStatusRequest updateJoinRequestStatusRequest,Long studentId){

        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId).orElseThrow(()->new JoinRequestNotFoundException("Request Not Found"));
        if(!joinRequest.getStatus().equals(JoinRequest.Status.PENDING)){
            throw new RequestRejectedException("JoinRequest Not Pending");
        }
        if(!joinRequest.getCollaborationRequest().getCreatedBy().getId().equals(studentId)){
            throw new AuthorizationException("You are not allowed to update this join request");
        }
        if (updateJoinRequestStatusRequest.getDecision() == UpdateJoinRequestStatusRequest.Decision.ACCEPTED) {
            if (collaborationMemberRepository.existsByCollaborationRequest_IdAndStudent_Id(joinRequest.getCollaborationRequest().getId(), joinRequest.getRequestedBy().getId())) {
                throw new AlreadyAcceptedAsTeamMemberException("Student is already a team member");
            }
            CollaborationMember collaborationMember =
                    CollaborationMember.builder()
                            .collaborationRequest(joinRequest.getCollaborationRequest())
                            .student(joinRequest.getRequestedBy())
                            .build();
            collaborationMemberRepository.save(collaborationMember);
            joinRequest.setStatus(JoinRequest.Status.ACCEPTED);
        } else {
            joinRequest.setStatus(JoinRequest.Status.REJECTED);
        }
        JoinRequest savedJoinRequest =joinRequestRepository.save(joinRequest);
        return JoinRequestResponse.builder()
                .id(savedJoinRequest.getId())
                .status(savedJoinRequest.getStatus())
                .createdAt(savedJoinRequest.getCreatedAt())
                .requestedByName(savedJoinRequest.getRequestedBy().getFullName())
                .requestedById(savedJoinRequest.getRequestedBy().getId())
                .collaborationRequestId(savedJoinRequest.getCollaborationRequest().getId())
                .message(savedJoinRequest.getMessage())
                .build();
    }
}
