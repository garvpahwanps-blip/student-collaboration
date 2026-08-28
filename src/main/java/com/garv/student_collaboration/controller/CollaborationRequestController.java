package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CollaborationRequestRequest;
import com.garv.student_collaboration.dto.CollaborationRequestResponse;
import com.garv.student_collaboration.service.CollaborationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollaborationRequestController {
    private final CollaborationRequestService collaborationRequestService;
    @PostMapping("/collaboration-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public CollaborationRequestResponse createCollaborationRequest(@Valid @RequestBody CollaborationRequestRequest collaborationRequestRequest){
        return collaborationRequestService.createCollaborationRequest(collaborationRequestRequest);
    }
    @GetMapping("/collaboration-requests/{id}")
    public CollaborationRequestResponse getCollaborationRequestById(@PathVariable Long id){
        return collaborationRequestService.getCollaborationRequestById(id);
    }
    @GetMapping("/students/{studentId}/collaboration-requests")
    public List<CollaborationRequestResponse> getCollaborationRequestsByStudentId(@PathVariable Long studentId){
        return collaborationRequestService.getCollaborationRequestsByStudentId(studentId);
    }
    @GetMapping("/collaboration-requests/open")
    public List<CollaborationRequestResponse> getOpenCollaborationRequests(){
        return collaborationRequestService.getOpenCollaborationRequests();
    }
}
