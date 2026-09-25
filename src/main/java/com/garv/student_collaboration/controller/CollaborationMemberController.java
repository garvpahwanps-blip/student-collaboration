package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.CollaborationMemberResponse;
import com.garv.student_collaboration.service.CollaborationMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Team Members",
        description = "View, remove, and leave collaboration teams"
)
@RestController
@RequestMapping("/api/collaboration-requests")
@RequiredArgsConstructor
public class CollaborationMemberController {
    private final CollaborationMemberService collaborationMemberService;
    @Operation(
            summary = "List team members",
            description = "Returns all members of a collaboration request."
    )
    @GetMapping("/{collaborationRequestId}/members")
    public List<CollaborationMemberResponse> getMembers(@PathVariable("collaborationRequestId") Long collaborationRequestId) {
        return collaborationMemberService.getMembersByCollaborationRequestId(collaborationRequestId);
    }
    @Operation(
            summary = "Remove a team member",
            description = "Allows the collaboration request creator to remove a member from the team."
    )
    @DeleteMapping("/{collaborationRequestId}/members/{memberStudentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable("collaborationRequestId") Long collaborationRequestId, @PathVariable("memberStudentId") Long memberStudentId, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        collaborationMemberService.removeMember(collaborationRequestId, memberStudentId, studentId);
    }
    @Operation(
            summary = "Leave a collaboration team",
            description = "Removes the authenticated student from a collaboration team."
    )
    @DeleteMapping("/{collaborationRequestId}/members/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveMember(@PathVariable("collaborationRequestId") Long collaborationRequestId, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        collaborationMemberService.leaveMember(collaborationRequestId, studentId);
    }
}

