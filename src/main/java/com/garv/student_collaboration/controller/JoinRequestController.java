package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CreateJoinRequestRequest;
import com.garv.student_collaboration.dto.JoinRequestResponse;
import com.garv.student_collaboration.dto.UpdateJoinRequestStatusRequest;
import com.garv.student_collaboration.service.JoinRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JoinRequestController {
    private final JoinRequestService joinRequestService;
    @PostMapping("/join-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public JoinRequestResponse createJoinRequest(@Valid @RequestBody CreateJoinRequestRequest createJoinRequestRequest, Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.createJoinRequest(createJoinRequestRequest, studentId);
    }
    @PatchMapping("/join-requests/{joinRequestId}/status")
    public  JoinRequestResponse updateJoinRequestStatus(@PathVariable Long joinRequestId, @Valid @RequestBody UpdateJoinRequestStatusRequest updateJoinRequestStatusRequest, Authentication authentication ){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.updateJoinRequestStatus(joinRequestId, updateJoinRequestStatusRequest, studentId);
    }
    @GetMapping("/join-requests/sent")
    public List<JoinRequestResponse> getSentJoinRequests(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.getSentJoinRequests(studentId);
    }
    @GetMapping("/join-requests/received")
    public List<JoinRequestResponse> getReceivedJoinRequests(Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return joinRequestService.getReceivedJoinRequests(studentId);
    }
    @DeleteMapping("/join-requests/{joinRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelJoinRequest(@PathVariable Long joinRequestId,Authentication authentication ){
        Long studentId = (Long) authentication.getPrincipal();
        joinRequestService.cancelJoinRequest(joinRequestId, studentId);
    }

}
