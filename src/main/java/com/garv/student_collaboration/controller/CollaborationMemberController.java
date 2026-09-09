package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CollaborationMemberResponse;
import com.garv.student_collaboration.service.CollaborationMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaboration-requests")
@RequiredArgsConstructor
public class CollaborationMemberController {
    private final CollaborationMemberService collaborationMemberService;
    @GetMapping("/{collaborationRequestId}/members")
    public List<CollaborationMemberResponse> getMembers(@PathVariable("collaborationRequestId") Long collaborationRequestId) {
        return collaborationMemberService.getMembersByCollaborationRequestId(collaborationRequestId);
    }
    @DeleteMapping("/{collaborationRequestId}/members/{memberStudentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable("collaborationRequestId") Long collaborationRequestId, @PathVariable("memberStudentId") Long memberStudentId, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        collaborationMemberService.removeMember(collaborationRequestId, memberStudentId, studentId);
    }
}
