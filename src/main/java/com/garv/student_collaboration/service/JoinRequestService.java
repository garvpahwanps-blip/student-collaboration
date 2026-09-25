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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final StudentRepository studentRepository;
    private  final CollaborationMemberRepository collaborationMemberRepository;
    private JoinRequestResponse toResponse(JoinRequest joinRequest){
        return JoinRequestResponse.builder()
                .id(joinRequest.getId())
                .collaborationRequestId(joinRequest.getCollaborationRequest().getId())
                .requestedById(joinRequest.getRequestedBy().getId())
                .message(joinRequest.getMessage())
                .requestedByName(joinRequest.getRequestedBy().getFullName())
                .createdAt(joinRequest.getCreatedAt())
                .status(joinRequest.getStatus())
                .build();

    }
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
        return toResponse(savedJoinRequest);

    }
    @Transactional
    public JoinRequestResponse updateJoinRequestStatus(Long joinRequestId, UpdateJoinRequestStatusRequest updateJoinRequestStatusRequest,Long studentId){

        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId).orElseThrow(()->new JoinRequestNotFoundException("Request Not Found"));
        if(!joinRequest.getStatus().equals(JoinRequest.Status.PENDING)){
            throw new RequestRejectedException("JoinRequest is Not Pending");
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
        return toResponse(savedJoinRequest);
    }
    @Transactional(readOnly = true)
    public List<JoinRequestResponse> getSentJoinRequests(Long studentId){
        if(!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException("Student Not Found");
        }
        List<JoinRequest> joinRequests = joinRequestRepository.findAllByRequestedBy_Id(studentId);
        List<JoinRequestResponse> joinRequestResponseList = new ArrayList<>();
        for (JoinRequest joinRequest : joinRequests) {
            joinRequestResponseList.add(toResponse(joinRequest));
        }
        return joinRequestResponseList;
    }
    @Transactional(readOnly = true)
    public List<JoinRequestResponse> getReceivedJoinRequests(Long studentId){
        if(!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException("Student Not Found");
        }
        List<JoinRequest> joinRequests = joinRequestRepository.findAllByCollaborationRequest_CreatedBy_Id(studentId);
        List<JoinRequestResponse> joinRequestResponseList = new ArrayList<>();
        for (JoinRequest joinRequest : joinRequests) {
            joinRequestResponseList.add(toResponse(joinRequest));
        }
        return joinRequestResponseList;
    }
    @Transactional
    public void cancelJoinRequest(Long joinRequestId,Long authenticatedStudentId){
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId).orElseThrow(()->new JoinRequestNotFoundException("Request Not Found"));
        if(!joinRequest.getRequestedBy().getId().equals(authenticatedStudentId)){
            throw new AuthorizationException("You are not allowed to cancel join request");
        }
        if(!joinRequest.getStatus().equals(JoinRequest.Status.PENDING)){
            throw new RequestRejectedException("JoinRequest Not Pending");
        }
        joinRequestRepository.delete(joinRequest);
    }
}
