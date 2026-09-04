package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.GoalRequest;
import com.garv.student_collaboration.dto.GoalResponse;
import com.garv.student_collaboration.dto.UpdateGoalStatusRequest;
import com.garv.student_collaboration.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    @PostMapping("/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse createGoal(@Valid @RequestBody GoalRequest goalRequest, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return goalService.createGoal(goalRequest,studentId);
    }
    @GetMapping("/goals/{id}")
    public GoalResponse getGoalById(@PathVariable Long id){
        return goalService.getGoalById(id);
    }
    @GetMapping("/students/{studentId}/goals")
    public List<GoalResponse> getGoalsByStudentId(@PathVariable Long studentId){
        return goalService.getGoalsByStudentId(studentId);
    }
    @PatchMapping("/goals/{id}/status")
    public GoalResponse updateGoalStatus(@PathVariable Long id, @Valid @RequestBody UpdateGoalStatusRequest updateGoalStatusRequest,Authentication authentication){
        Long studentId = (Long) authentication.getPrincipal();
        return goalService.updateGoalStatus(id, updateGoalStatusRequest,studentId);
    }
}
