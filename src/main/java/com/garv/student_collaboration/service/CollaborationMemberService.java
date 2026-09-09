package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.CollaborationMemberResponse;
import com.garv.student_collaboration.entity.CollaborationMember;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.exception.AuthorizationException;
import com.garv.student_collaboration.exception.CollaborationMemberNotFoundException;
import com.garv.student_collaboration.exception.CollaborationRequestNotFoundException;
import com.garv.student_collaboration.repository.CollaborationMemberRepository;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationMemberService {
    private final CollaborationMemberRepository collaborationMemberRepository;
    private final CollaborationRequestRepository collaborationRequestRepository;
    private CollaborationMemberResponse toResponse(CollaborationMember collaborationMember) {
        return CollaborationMemberResponse.builder()
                .collaborationRequestId(collaborationMember.getCollaborationRequest().getId())
                .id(collaborationMember.getId())
                .joinedAt(collaborationMember.getJoinedAt())
                .studentId(collaborationMember.getStudent().getId())
                .studentName(collaborationMember.getStudent().getFullName())
                .build();
    }
    @Transactional(readOnly = true)
    public List<CollaborationMemberResponse> getMembersByCollaborationRequestId(Long collaborationRequestId) {
        if(!collaborationRequestRepository.existsById(collaborationRequestId)) {
            throw new CollaborationRequestNotFoundException("CollaborationRequest not found");
        }
        List<CollaborationMember> members = collaborationMemberRepository.findAllByCollaborationRequest_Id(collaborationRequestId);
        List<CollaborationMemberResponse> memberResponses = new ArrayList<>();
        for (CollaborationMember member : members) {
            memberResponses.add(toResponse(member));
        }
        return memberResponses;
    }
    @Transactional
    public void removeMember(Long collaborationRequestId,Long memberStudentId,Long authenticatedStudentId){
        CollaborationRequest collaborationRequest = collaborationRequestRepository.findById(collaborationRequestId).orElseThrow(()-> new CollaborationRequestNotFoundException("CollaborationRequest not found"));
        if(!collaborationRequest.getCreatedBy().getId().equals(authenticatedStudentId)){
            throw new AuthorizationException("You are not authorized to perform this operation");
        }
        CollaborationMember member = collaborationMemberRepository.findByCollaborationRequest_IdAndStudent_Id(collaborationRequestId,memberStudentId).orElseThrow(()->new CollaborationMemberNotFoundException("CollaborationMember not found"));
        collaborationMemberRepository.delete(member);
    }
}
