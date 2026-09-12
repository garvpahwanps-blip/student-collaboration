package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.UpdateJoinRequestStatusRequest;
import com.garv.student_collaboration.entity.CollaborationMember;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.JoinRequest;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.AlreadyAcceptedAsTeamMemberException;
import com.garv.student_collaboration.exception.AuthorizationException;
import com.garv.student_collaboration.exception.JoinRequestNotFoundException;
import com.garv.student_collaboration.exception.RequestRejectedException;
import com.garv.student_collaboration.repository.CollaborationMemberRepository;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.JoinRequestRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class JoinRequestServiceTest {
    @Mock
    private JoinRequestRepository joinRequestRepository;
    @Mock
    private CollaborationRequestRepository collaborationRequestRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CollaborationMemberRepository collaborationMemberRepository;
    @InjectMocks
    private JoinRequestService joinRequestService;
    private Long joinRequestId;
    private Long creatorId;
    private Long requesterId;

    private Student creator;
    private Student requester;
    private CollaborationRequest collaborationRequest;
    private JoinRequest joinRequest;
    @BeforeEach
    void setUp() {
        joinRequestId = 1L;
        creatorId = 10L;
        requesterId = 20L;

        creator = Student.builder().id(creatorId).build();
        requester = Student.builder().id(requesterId).build();
        collaborationRequest = CollaborationRequest.builder().id(5L).createdBy(creator).build();
        joinRequest = JoinRequest.builder().id(joinRequestId).collaborationRequest(collaborationRequest).requestedBy(requester).build();
        joinRequest.setStatus(JoinRequest.Status.PENDING);
    }
    @Test
    void cancelJoinRequest_shouldDeletePendingRequestOwnedByStudent(){
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        joinRequestService.cancelJoinRequest(joinRequestId, requesterId);
        verify(joinRequestRepository).delete(joinRequest);
    }
    @Test
    void cancelJoinRequest_shouldThrowExceptionWhenStudentIsNotOwner(){
        Long anotherStudentId = 3L;
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        assertThrows(AuthorizationException.class,()->joinRequestService.cancelJoinRequest(joinRequestId, anotherStudentId));
        verify(joinRequestRepository,never()).delete(joinRequest);
    }
    @Test
    void cancelJoinRequest_shouldThrowExceptionWhenStatusIsNotPending(){
        joinRequest.setStatus(JoinRequest.Status.ACCEPTED);
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        assertThrows(RequestRejectedException.class,()->joinRequestService.cancelJoinRequest(joinRequestId, requesterId));
        verify(joinRequestRepository,never()).delete(joinRequest);
    }
    @Test
    void cancelJoinRequest_shouldThrowExceptionWhenJoinRequestDoesNotExist(){
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.empty());
        assertThrows(JoinRequestNotFoundException.class,()->joinRequestService.cancelJoinRequest(joinRequestId, requesterId));
        verify(joinRequestRepository,never()).delete(any(JoinRequest.class));
    }
    @Test
    void updateJoinRequestStatus_shouldAcceptRequestAndCreateMember(){
        UpdateJoinRequestStatusRequest request = UpdateJoinRequestStatusRequest.builder().decision(UpdateJoinRequestStatusRequest.Decision.ACCEPTED).build();
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        when(collaborationMemberRepository.existsByCollaborationRequest_IdAndStudent_Id(collaborationRequest.getId(),requesterId))
        .thenReturn(false);
        when(joinRequestRepository.save(joinRequest)).thenReturn(joinRequest);
        joinRequestService.updateJoinRequestStatus(joinRequestId,request,creatorId);
        assertEquals(JoinRequest.Status.ACCEPTED, joinRequest.getStatus());
        ArgumentCaptor<CollaborationMember> memberCaptor = ArgumentCaptor.forClass(CollaborationMember.class);
        verify(collaborationMemberRepository).save(memberCaptor.capture());
        verify(joinRequestRepository).save(joinRequest);
        CollaborationMember savedMember = memberCaptor.getValue();
        assertSame(collaborationRequest, savedMember.getCollaborationRequest());
        assertSame(requester, savedMember.getStudent());

    }
    @Test
    void updateJoinRequestStatus_shouldRejectRequestWithoutCreatingMember(){
        UpdateJoinRequestStatusRequest request = UpdateJoinRequestStatusRequest.builder().decision(UpdateJoinRequestStatusRequest.Decision.REJECTED).build();
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        when(joinRequestRepository.save(joinRequest)).thenReturn(joinRequest);
        joinRequestService.updateJoinRequestStatus(joinRequestId,request,creatorId);
        assertEquals(JoinRequest.Status.REJECTED, joinRequest.getStatus());
        verify(joinRequestRepository).save(joinRequest);
        verifyNoInteractions(collaborationMemberRepository);
    }
    @Test
    void updateJoinRequestStatus_shouldThrowExceptionWhenStudentIsNotRequestCreator(){

        Long anotherStudentId = 99L;
        UpdateJoinRequestStatusRequest request = UpdateJoinRequestStatusRequest.builder().decision(UpdateJoinRequestStatusRequest.Decision.ACCEPTED).build();
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        assertThrows(AuthorizationException.class,()->joinRequestService.updateJoinRequestStatus(joinRequestId,request,anotherStudentId));
        verifyNoInteractions(collaborationMemberRepository);
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
    }
    @Test
    void updateJoinRequestStatus_shouldThrowExceptionWhenStudentIsAlreadyATeamMember(){
        UpdateJoinRequestStatusRequest request = UpdateJoinRequestStatusRequest.builder().decision(UpdateJoinRequestStatusRequest.Decision.ACCEPTED).build();
        when(joinRequestRepository.findById(joinRequestId)).thenReturn(Optional.of(joinRequest));
        when(collaborationMemberRepository
                .existsByCollaborationRequest_IdAndStudent_Id(5L, requesterId))
                .thenReturn(true);
        assertThrows(AlreadyAcceptedAsTeamMemberException.class,()->joinRequestService.updateJoinRequestStatus(joinRequestId,request,creatorId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
        verify(collaborationMemberRepository, never()).save(any(CollaborationMember.class));
    }
}
