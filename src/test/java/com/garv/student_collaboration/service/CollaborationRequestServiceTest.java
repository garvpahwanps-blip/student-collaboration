package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.CollaborationRequestRequest;
import com.garv.student_collaboration.dto.CollaborationRequestResponse;
import com.garv.student_collaboration.dto.UpdateCollaborationRequestRequest;
import com.garv.student_collaboration.dto.UpdateCollaborationRequestStatusRequest;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.Goal;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.*;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.GoalRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CollaborationRequestServiceTest {
    @Mock
    private CollaborationRequestRepository collaborationRequestRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private GoalRepository goalRepository;
    @InjectMocks
    private CollaborationRequestService collaborationRequestService;

    private Long collaborationRequestId;
    private Long ownerId;

    private Student owner;
    private Goal goal;
    private CollaborationRequest collaborationRequest;

    @BeforeEach
    void setUp() {
        collaborationRequestId = 1L;
        ownerId = 10L;

        owner = Student.builder()
                .id(ownerId)
                .build();

        goal = Goal.builder()
                .id(5L)
                .student(owner)
                .build();

        collaborationRequest = CollaborationRequest.builder()
                .id(collaborationRequestId)
                .createdBy(owner)
                .goal(goal)
                .build();

        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
    }
    @Test
    void updateCollaborationRequestStatus_shouldUpdateStatusWhenCalledByOwner(){
        UpdateCollaborationRequestStatusRequest request = new UpdateCollaborationRequestStatusRequest();
        request.setStatus(CollaborationRequest.Status.CLOSED);
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        when(collaborationRequestRepository.save(collaborationRequest)).thenReturn(collaborationRequest);
        collaborationRequestService.updateCollaborationRequestStatus(collaborationRequestId,request,ownerId);
        assertEquals(CollaborationRequest.Status.CLOSED,collaborationRequest.getStatus());
        verify(collaborationRequestRepository).save(collaborationRequest);
    }
    @Test
    void updateCollaborationRequestStatus_shouldThrowExceptionWhenNotCalledByOwner(){
        UpdateCollaborationRequestStatusRequest request = new UpdateCollaborationRequestStatusRequest();
        request.setStatus(CollaborationRequest.Status.CLOSED);
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        assertThrows(AuthorizationException.class,()->collaborationRequestService.updateCollaborationRequestStatus(collaborationRequestId,request,99L));
        verify(collaborationRequestRepository,never()).save(any(CollaborationRequest.class));
    }
    @Test
    void updateCollaborationRequestStatus_shouldThrowExceptionWhenCollaborationRequestDoesNotExist(){
        UpdateCollaborationRequestStatusRequest request = new UpdateCollaborationRequestStatusRequest();
        request.setStatus(CollaborationRequest.Status.CLOSED);
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.empty());
        assertThrows(CollaborationRequestNotFoundException.class,()->collaborationRequestService.updateCollaborationRequestStatus(collaborationRequestId,request,ownerId));
        verify(collaborationRequestRepository, never()).save(any(CollaborationRequest.class));
    }
    @Test
    void updateCollaborationRequest_shouldUpdateOnlyProvidedFieldsWhenCalledByOwners(){
        collaborationRequest.setTitle("Original title");
        collaborationRequest.setDescription("Keep this description");
        collaborationRequest.setWeeklyHours(5);
        UpdateCollaborationRequestRequest request = UpdateCollaborationRequestRequest.builder().title("Updated Title").weeklyHours(12).build();
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        when(collaborationRequestRepository.save(collaborationRequest)).thenReturn(collaborationRequest);
        collaborationRequestService.updateCollaborationRequest(collaborationRequestId,request,ownerId);
        assertEquals("Updated Title",collaborationRequest.getTitle());
        assertEquals("Keep this description",collaborationRequest.getDescription());
        assertEquals(12,collaborationRequest.getWeeklyHours());
        verify(collaborationRequestRepository).save(collaborationRequest);
    }
    @Test
    void updateCollaborationRequest_shouldThrowExceptionWhenStudentIsNotOwner(){
        UpdateCollaborationRequestRequest request = UpdateCollaborationRequestRequest.builder().title("Updated Title").weeklyHours(12).build();
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.of(collaborationRequest));
        assertThrows(AuthorizationException.class,()->collaborationRequestService.updateCollaborationRequest(collaborationRequestId,request,99L));
        verify(collaborationRequestRepository,never()).save(any(CollaborationRequest.class));
    }
    @Test
    void updateCollaborationRequest_shouldThrowExceptionWhenCollaborationRequestDoesNotExist(){
        UpdateCollaborationRequestRequest request = UpdateCollaborationRequestRequest.builder().title("Updated Title").weeklyHours(12).build();
        when(collaborationRequestRepository.findById(collaborationRequestId)).thenReturn(Optional.empty());
        assertThrows(CollaborationRequestNotFoundException.class,()->collaborationRequestService.updateCollaborationRequest(collaborationRequestId,request,ownerId));
        verify(collaborationRequestRepository,never()).save(any(CollaborationRequest.class));
    }
    @Test
    void createCollaborationRequest_shouldSaveOpenRequestWhenGoalBelongsToOwner(){
        CollaborationRequestRequest request = CollaborationRequestRequest.builder()
                        .goalId(goal.getId())
                        .title("Build a study group")
                        .description("Looking for Java learners")
                        .weeklyHours(6)
                        .build();
        when(studentRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));
        when(collaborationRequestRepository.save(any(CollaborationRequest.class)))
                .thenAnswer(invocation -> {
                    CollaborationRequest requestToSave = invocation.getArgument(0);
                    requestToSave.setId(100L);
                    return requestToSave;
                });
        CollaborationRequestResponse response=collaborationRequestService.createCollaborationRequest(request,ownerId);
        ArgumentCaptor<CollaborationRequest> requestCaptor = ArgumentCaptor.forClass(CollaborationRequest.class);
        verify(collaborationRequestRepository).save(requestCaptor.capture());
        CollaborationRequest savedRequest=requestCaptor.getValue();
        assertSame(owner,savedRequest.getCreatedBy());
        assertSame(goal,savedRequest.getGoal());
        assertEquals(CollaborationRequest.Status.OPEN,savedRequest.getStatus());
        assertEquals("Build a study group",savedRequest.getTitle());
        assertEquals(100L,response.getId());
        assertEquals(ownerId,response.getCreatedById());
        assertEquals(goal.getId(),response.getGoalId());

    }
    @Test
    void createCollaborationRequest_shouldThrowExceptionWhenGoalDoesNotBelongToOwner(){
        Student otherStudent = Student.builder().id(99L).build();
        Goal foreingGoal = Goal.builder().id(22L).student(otherStudent).build();
        CollaborationRequestRequest request = CollaborationRequestRequest.builder()
                .goalId(foreingGoal.getId())
                .title("Build a study group")
                .description("Looking for Java learners")
                .weeklyHours(6)
                .build();
        when(studentRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(goalRepository.findById(foreingGoal.getId())).thenReturn(Optional.of(foreingGoal));
        assertThrows(GoalNotBelongsToStudentException.class,()->collaborationRequestService.createCollaborationRequest(request,ownerId));
        verifyNoInteractions(collaborationRequestRepository);

    }
    @Test
    void createCollaborationRequest_shouldThrowExceptionStudentDoesNotExist(){
        CollaborationRequestRequest request = CollaborationRequestRequest.builder()
                .goalId(goal.getId())
                .title("Build a study group")
                .description("Looking for Java learners")
                .weeklyHours(6)
                .build();
        when(studentRepository.findById(ownerId)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class,()->collaborationRequestService.createCollaborationRequest(request,ownerId));
        verifyNoInteractions(goalRepository,collaborationRequestRepository);

    }
    @Test
    void createCollaborationRequest_shouldThrowExceptionWhenGoalDoesNotExists(){
        CollaborationRequestRequest request = CollaborationRequestRequest.builder()
                .goalId(goal.getId())
                .title("Build a study group")
                .description("Looking for Java learners")
                .weeklyHours(6)
                .build();
        when(studentRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(goalRepository.findById(goal.getId())).thenReturn(Optional.empty());
        assertThrows(GoalNotFoundException.class,()->collaborationRequestService.createCollaborationRequest(request,ownerId));
        verifyNoInteractions(collaborationRequestRepository);
    }
}
