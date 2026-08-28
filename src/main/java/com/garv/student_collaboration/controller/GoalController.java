package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.GoalRequest;
import com.garv.student_collaboration.dto.GoalResponse;
import com.garv.student_collaboration.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    @PostMapping("/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse createGoal(@Valid @RequestBody GoalRequest goalRequest){
        return goalService.createGoal(goalRequest);
    }
    @GetMapping("/goals/{id}")
    public GoalResponse getGoalById(@PathVariable Long id){
        return goalService.getGoalById(id);
    }
    @GetMapping("/students/{id}/goals")
    public List<GoalResponse> getGoalsByStudentId(@PathVariable Long studentId){
        return goalService.getGoalsByStudentId(studentId);
    }
}
