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
                .existsByCollaborationRequest_IdAndStudent_Id(collaborationRequest.getId(), requesterId))
                .thenReturn(true);
        assertThrows(AlreadyAcceptedAsTeamMemberException.class,()->joinRequestService.updateJoinRequestStatus(joinRequestId,request,creatorId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
        verify(collaborationMemberRepository, never()).save(any(CollaborationMember.class));
    }
    @Test
    void createJoinRequest_shouldCreateNewJoinRequest(){
        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.of(collaborationRequest));
        when(studentRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(joinRequestRepository.existsByCollaborationRequest_IdAndRequestedBy_Id(collaborationRequest.getId(), requesterId)).thenReturn(false);
        when(joinRequestRepository.save((any(JoinRequest.class)))).thenAnswer(invocation ->
        {
            JoinRequest requestToSave = invocation.getArgument(0);
            requestToSave.setId(100L);
            return requestToSave;
        });
        JoinRequestResponse response = joinRequestService.createJoinRequest(request,requesterId);
        ArgumentCaptor<JoinRequest> joinRequestCaptor = ArgumentCaptor.forClass(JoinRequest.class);
        verify(joinRequestRepository).save(joinRequestCaptor.capture());
        JoinRequest savedJoinRequest = joinRequestCaptor.getValue();
        assertSame(collaborationRequest,savedJoinRequest.getCollaborationRequest());
        assertSame(requester,savedJoinRequest.getRequestedBy());
        assertEquals("I would like to join the project",response.getMessage());
        assertEquals(100L,response.getId());
        assertEquals(collaborationRequest.getId(),response.getCollaborationRequestId());
        assertEquals(requesterId,response.getRequestedById());
    }
    @Test
    void createJoinRequest_shouldThrowExceptionWhenCollaborationRequestIsClosed(){
        collaborationRequest.setStatus(CollaborationRequest.Status.CLOSED);
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.of(collaborationRequest));
        when(studentRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        assertThrows(RequestRejectedException.class, ()->joinRequestService.createJoinRequest(request,requesterId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
    }
    @Test
    void createJoinRequest_shouldThrowExceptionWhenOwnerTriesToSendRequestToItsOwnRequest(){
        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.of(collaborationRequest));
        when(studentRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        assertThrows(RequestRejectedException.class, ()->joinRequestService.createJoinRequest(request,creatorId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));

    }
    @Test
    void createJoinRequest_shouldThrowExceptionWhenDuplicateRequestExists(){
        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.of(collaborationRequest));
        when(studentRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(joinRequestRepository.existsByCollaborationRequest_IdAndRequestedBy_Id(collaborationRequest.getId(), requesterId)).thenReturn(true);
        assertThrows(RequestRejectedException.class,()->joinRequestService.createJoinRequest(request,requesterId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
    }
    @Test
    void createJoinRequest_shouldThrowExceptionWhenCollaborationRequestDoesNotExists(){
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.empty());
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        assertThrows(CollaborationRequestNotFoundException.class,()->joinRequestService.createJoinRequest(request,requesterId));
        verify(joinRequestRepository,never()).save(any(JoinRequest.class));
        verifyNoInteractions(studentRepository);
    }
    @Test
    void createJoinRequest_shouldThrowExceptionWhenStudentDoesNotExists(){
        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
        CreateJoinRequestRequest request = CreateJoinRequestRequest.builder().collaborationRequestId(collaborationRequest.getId()).message("I would like to join the project").build();
        when(collaborationRequestRepository.findById(collaborationRequest.getId())).thenReturn(Optional.of(collaborationRequest));
        when(studentRepository.findById(requesterId)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class,()->joinRequestService.createJoinRequest(request,requesterId));
        verifyNoInteractions(joinRequestRepository);

    }
}
