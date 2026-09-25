package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.GoalRequest;
import com.garv.student_collaboration.dto.GoalResponse;
import com.garv.student_collaboration.dto.UpdateGoalStatusRequest;
import com.garv.student_collaboration.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Goals",
        description = "Create, view, and manage personal learning or project goals"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    @Operation(
            summary = "Create a goal",
            description = "Creates a goal for the authenticated student."
    )
    @PostMapping("/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse createGoal(@Valid @RequestBody GoalRequest goalRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return goalService.createGoal(goalRequest,studentId);
    }
    @Operation(
            summary = "Get a goal",
            description = "Returns one goal by its ID."
    )
    @GetMapping("/goals/{id}")
    public GoalResponse getGoalById(@PathVariable Long id){
        return goalService.getGoalById(id);
    }
    @Operation(
            summary = "List goals",
            description = "Returns goals for a student."
    )
    @GetMapping("/students/{studentId}/goals")
    public List<GoalResponse> getGoalsByStudentId(@PathVariable Long studentId){
        return goalService.getGoalsByStudentId(studentId);
    }
    @Operation(
            summary = "Update goal status",
            description = "Updates the status of a goal owned by the authenticated student."
    )
    @PatchMapping("/goals/{id}/status")
    public GoalResponse updateGoalStatus(@PathVariable Long id, @Valid @RequestBody UpdateGoalStatusRequest updateGoalStatusRequest,Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return goalService.updateGoalStatus(id, updateGoalStatusRequest,studentId);
    }
}
