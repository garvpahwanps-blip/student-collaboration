package com.garv.student_collaboration.service;

import com.garv.student_collaboration.entity.CollaborationMember;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.AuthorizationException;
import com.garv.student_collaboration.exception.CollaborationMemberNotFoundException;
import com.garv.student_collaboration.exception.CollaborationRequestNotFoundException;
import com.garv.student_collaboration.repository.CollaborationMemberRepository;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollaborationMemberServiceTest {
    @Mock
    private CollaborationMemberRepository collaborationMemberRepository;
    @Mock
    private CollaborationRequestRepository collaborationRequestRepository;
    @InjectMocks
    private CollaborationMemberService collaborationMemberService;
    private Long collaborationRequestId;
    private Long creatorId;
    private Long memberStudentId;

    private Student creator;
    private Student memberStudent;
    private CollaborationRequest collaborationRequest;
    private CollaborationMember collaborationMember;

    @BeforeEach
    void setUp() {
        collaborationRequestId = 1L;
        creatorId = 10L;
        memberStudentId = 20L;

        creator = Student.builder()
                .id(creatorId)
                .build();

        memberStudent = Student.builder()
                .id(memberStudentId)
                .build();

        collaborationRequest = CollaborationRequest.builder()
                .id(collaborationRequestId)
                .createdBy(creator)
                .build();

        collaborationMember = CollaborationMember.builder()
                .collaborationRequest(collaborationRequest)
                .student(memberStudent)
                .build();
    }
    @Test
    void removeMember_shouldDeleteMemberWhenCalledByRequestCreator(){
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        when(collaborationMemberRepository.findByCollaborationRequest_IdAndStudent_Id(collaborationRequestId, memberStudentId)).thenReturn(Optional.of(collaborationMember));
        collaborationMemberService.removeMember(collaborationRequestId,memberStudentId,creatorId);
        verify(collaborationMemberRepository).delete(collaborationMember);
    }
    @Test
    void removeMember_shouldThrowExceptionWhenNotCalledByRequestCreator(){
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        assertThrows(AuthorizationException.class,()-> collaborationMemberService.removeMember(collaborationRequestId,memberStudentId,99L));
        verifyNoInteractions(collaborationMemberRepository);
    }
    @Test
    void removeMember_shouldThrowExceptionWhenCollaborationMemberDoesNotExist(){
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        when(collaborationMemberRepository.findByCollaborationRequest_IdAndStudent_Id(collaborationRequestId, memberStudentId)).thenReturn(Optional.empty());
        assertThrows(CollaborationMemberNotFoundException.class,()->collaborationMemberService.removeMember(collaborationRequestId,memberStudentId,creatorId));
        verify(collaborationMemberRepository,never()).delete(collaborationMember);

    }
    @Test
    void leaveMember_shouldDeleteWhenCalledByTheMember(){
        when(collaborationRequestRepository.existsById(collaborationRequestId)).thenReturn(true);
        when(collaborationMemberRepository.findByCollaborationRequest_IdAndStudent_Id(collaborationRequestId, memberStudentId)).thenReturn(Optional.of(collaborationMember));
        collaborationMemberService.leaveMember(collaborationRequestId,memberStudentId);
        verify(collaborationMemberRepository).delete(collaborationMember);
    }
    @Test
    void leaveMember_shouldThrowExceptionWhenCollaborationRequestDoesNotExist(){
        when(collaborationRequestRepository.existsById(collaborationRequestId)).thenReturn(false);
        assertThrows(CollaborationRequestNotFoundException.class,()-> collaborationMemberService.leaveMember(collaborationRequestId,memberStudentId));
        verifyNoInteractions(collaborationMemberRepository);
    }
    @Test
    void leaveMember_shouldThrowExceptionWhenCollaborationMemberDoesNotExist(){
        when(collaborationRequestRepository.existsById(collaborationRequestId)).thenReturn(true);
        when(collaborationMemberRepository.findByCollaborationRequest_IdAndStudent_Id(collaborationRequestId, memberStudentId)).thenReturn(Optional.empty());
        assertThrows(CollaborationMemberNotFoundException.class,()->collaborationMemberService.leaveMember(collaborationRequestId,memberStudentId));
        verify(collaborationMemberRepository,never()).delete(collaborationMember);
    }
}
