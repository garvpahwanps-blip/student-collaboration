package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CollaborationRequestRequest;
import com.garv.student_collaboration.dto.CollaborationRequestResponse;
import com.garv.student_collaboration.dto.UpdateCollaborationRequestRequest;
import com.garv.student_collaboration.dto.UpdateCollaborationRequestStatusRequest;
import com.garv.student_collaboration.service.CollaborationRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Collaboration Requests",
        description = "Create, view, update, and manage collaboration requests"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollaborationRequestController {
    private final CollaborationRequestService collaborationRequestService;
    @Operation(
            summary = "Create a collaboration request",
            description = "Creates an open collaboration request for a goal owned by the authenticated student."
    )
    @PostMapping("/collaboration-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public CollaborationRequestResponse createCollaborationRequest(@Valid @RequestBody CollaborationRequestRequest collaborationRequestRequest, Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return collaborationRequestService.createCollaborationRequest(collaborationRequestRequest,studentId);
    }
    @Operation(
            summary = "Get a collaboration request",
            description = "Returns one collaboration request by its ID."
    )
    @GetMapping("/collaboration-requests/{id}")
    public CollaborationRequestResponse getCollaborationRequestById(@PathVariable Long id){
        return collaborationRequestService.getCollaborationRequestById(id);
    }
    @Operation(
            summary = "List collaboration requests",
            description = "Returns available collaboration requests."
    )
    @GetMapping("/students/{studentId}/collaboration-requests")
    public List<CollaborationRequestResponse> getCollaborationRequestsByStudentId(@PathVariable Long studentId){
        return collaborationRequestService.getCollaborationRequestsByStudentId(studentId);
    }
    @Operation(
            summary = "List open collaboration requests",
            description = "Returns only collaboration requests that are currently open for students to join."
    )
    @GetMapping("/collaboration-requests/open")
    public List<CollaborationRequestResponse> getOpenCollaborationRequests(){
        return collaborationRequestService.getOpenCollaborationRequests();
    }
    @Operation(
            summary = "Change collaboration request status",
            description = "Opens or closes a collaboration request owned by the authenticated student."
    )
    @PatchMapping("/collaboration-requests/{collaborationRequestId}/status")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationRequestResponse updateRequestStatus(@PathVariable Long collaborationRequestId, @Valid @RequestBody UpdateCollaborationRequestStatusRequest request, Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return collaborationRequestService.updateCollaborationRequestStatus(collaborationRequestId,request,studentId);
    }
    @Operation(
            summary = "Update a collaboration request",
            description = "Updates selected details of a collaboration request owned by the authenticated student."
    )
    @PatchMapping("/collaboration-requests/{collaborationRequestId}")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationRequestResponse updateRequest(@PathVariable Long collaborationRequestId, @Valid @RequestBody UpdateCollaborationRequestRequest request, Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return collaborationRequestService.updateCollaborationRequest(collaborationRequestId,request,studentId);
    }
}
