package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.CollaborationRequestRequest;
import com.garv.student_collaboration.dto.CollaborationRequestResponse;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.Goal;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.CollaborationRequestNotFoundException;
import com.garv.student_collaboration.exception.GoalNotBelongsToStudentException;
import com.garv.student_collaboration.exception.GoalNotFoundException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.GoalRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaborationRequestService {
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final StudentRepository studentRepository;
    private final GoalRepository goalRepository;
    private CollaborationRequestResponse toResponse(CollaborationRequest collaborationRequest){
        return CollaborationRequestResponse.builder()
                .id(collaborationRequest.getId())
                .createdById(collaborationRequest.getCreatedBy().getId())
                .goalId(collaborationRequest.getGoal().getId())
                .collaborationMode(collaborationRequest.getCollaborationMode())
                .description(collaborationRequest.getDescription())
                .status(collaborationRequest.getStatus())
                .title(collaborationRequest.getTitle())
                .weeklyHours(collaborationRequest.getWeeklyHours())
                .build();
    }
    private CollaborationRequest toEntity(CollaborationRequestRequest collaborationRequest, Student createdBy, Goal goal){
        return CollaborationRequest.builder()
                .createdBy(createdBy)
                .goal(goal)
                .collaborationMode(collaborationRequest.getCollaborationMode())
                .description(collaborationRequest.getDescription())
                .title(collaborationRequest.getTitle())
                .weeklyHours(collaborationRequest.getWeeklyHours())
                .build();
    }
    public CollaborationRequestResponse createCollaborationRequest(CollaborationRequestRequest collaborationRequestRequest,Long studentId){
        Student createdBy = studentRepository.findById(studentId).orElseThrow(()->new StudentNotFoundException("student not found"));
        Goal goal = goalRepository.findById(collaborationRequestRequest.getGoalId()).orElseThrow(()->new GoalNotFoundException("goal not found"));

        if(!(goal.getStudent().getId().equals(createdBy.getId()))){
            throw  new GoalNotBelongsToStudentException("Goal does not belong to the authenticated student");
        }
        CollaborationRequest collaborationRequest =  toEntity(collaborationRequestRequest, createdBy, goal);
        collaborationRequest.setStatus(CollaborationRequest.Status.OPEN);
        return toResponse(collaborationRequestRepository.save(collaborationRequest));
    }
    @Transactional(readOnly = true)
    public CollaborationRequestResponse getCollaborationRequestById(Long id){
        CollaborationRequest collaborationRequest = collaborationRequestRepository.findById(id).orElseThrow(()->new CollaborationRequestNotFoundException("collaboration request not found"));
        return toResponse(collaborationRequest);
    }
    @Transactional(readOnly = true)
    public List<CollaborationRequestResponse> getCollaborationRequestsByStudentId(Long studentId){
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("student not found");
        }
        List<CollaborationRequest> collaborationRequestsList = collaborationRequestRepository.findAllByCreatedBy_Id(studentId);
        List<CollaborationRequestResponse> collaborationRequestResponseList = new ArrayList<>();
        for(CollaborationRequest collaborationRequest : collaborationRequestsList){
            collaborationRequestResponseList.add(toResponse(collaborationRequest));
        }
        return collaborationRequestResponseList;

    }
    @Transactional(readOnly = true)
    public List<CollaborationRequestResponse> getOpenCollaborationRequests(){
        List<CollaborationRequest> openCollaborationRequests = collaborationRequestRepository.findAllByStatus(CollaborationRequest.Status.OPEN);
        List<CollaborationRequestResponse> collaborationRequestResponseList = new ArrayList<>();
        for(CollaborationRequest collaborationRequest : openCollaborationRequests){
            collaborationRequestResponseList.add(toResponse(collaborationRequest));
        }
        return collaborationRequestResponseList;
    }

}
