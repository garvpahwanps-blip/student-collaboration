package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CreateJoinRequestRequest;
import com.garv.student_collaboration.dto.JoinRequestResponse;
import com.garv.student_collaboration.dto.UpdateJoinRequestStatusRequest;
import com.garv.student_collaboration.service.JoinRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Join Requests",
        description = "Send, review, view, and cancel requests to join collaboration teams"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JoinRequestController {
    private final JoinRequestService joinRequestService;
    @Operation(
            summary = "Send a join request",
            description = "Sends a request for the authenticated student to join an open collaboration request."
    )
    @PostMapping("/join-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public JoinRequestResponse createJoinRequest(@Valid @RequestBody CreateJoinRequestRequest createJoinRequestRequest, Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.createJoinRequest(createJoinRequestRequest, studentId);
    }
    @Operation(
            summary = "Accept or reject a join request",
            description = "Allows the collaboration request creator to accept or reject a pending join request."
    )
    @PatchMapping("/join-requests/{joinRequestId}/status")
    public  JoinRequestResponse updateJoinRequestStatus(@PathVariable Long joinRequestId, @Valid @RequestBody UpdateJoinRequestStatusRequest updateJoinRequestStatusRequest, Authentication authentication ){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.updateJoinRequestStatus(joinRequestId, updateJoinRequestStatusRequest, studentId);
    }
    @Operation(
            summary = "List sent join requests",
            description = "Returns join requests sent by the authenticated student."
    )
    @GetMapping("/join-requests/sent")
    public List<JoinRequestResponse> getSentJoinRequests(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.getSentJoinRequests(studentId);
    }
    @Operation(
            summary = "List received join requests",
            description = "Returns join requests received for collaboration requests owned by the authenticated student."
    )
    @GetMapping("/join-requests/received")
    public List<JoinRequestResponse> getReceivedJoinRequests(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.getReceivedJoinRequests(studentId);
    }
    @Operation(
            summary = "Cancel a join request",
            description = "Cancels a pending join request sent by the authenticated student."
    )
    @DeleteMapping("/join-requests/{joinRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelJoinRequest(@PathVariable Long joinRequestId,Authentication authentication ){
        Long studentId = (Long) authentication.getPrincipal();
        joinRequestService.cancelJoinRequest(joinRequestId, studentId);
    }

}
