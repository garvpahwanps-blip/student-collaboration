package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.GoalRequest;
import com.garv.student_collaboration.dto.GoalResponse;
import com.garv.student_collaboration.dto.UpdateGoalStatusRequest;
import com.garv.student_collaboration.entity.Goal;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.GoalNotFoundException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.GoalRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final StudentRepository studentRepository;
    private GoalResponse toResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .description(goal.getDescription())
                .priority(goal.getPriority())
                .status(goal.getStatus())
                .studentId(goal.getStudent().getId())
                .type(goal.getType())
                .build();
    }
    private Goal toEntity(GoalRequest goalRequest, Student student) {
        return Goal.builder()
                .description(goalRequest.getDescription())
                .priority(goalRequest.getPriority())
                .student(student)
                .type(goalRequest.getType())
                .build();
    }
    public GoalResponse createGoal(GoalRequest goalRequest) {
        Student student = studentRepository.findById(goalRequest.getStudentId()).orElseThrow(()-> new StudentNotFoundException("Student not found"));
        Goal goal = toEntity(goalRequest, student);
        goal.setStatus(Goal.Status.NOT_STARTED);
        return toResponse(goalRepository.save(goal));
    }
    @Transactional(readOnly = true)
    public GoalResponse getGoalById(Long id) {
        Goal goal = goalRepository.findById(id).orElseThrow(() -> new GoalNotFoundException("Goal not found"));
        return toResponse(goal);
    }
    @Transactional(readOnly = true)
    public List<GoalResponse> getGoalsByStudentId(Long studentId) {
        if(studentRepository.findById(studentId).isEmpty()) {
            throw new StudentNotFoundException("Student not found");
        }
        List<Goal> goals = goalRepository.findAllByStudent_Id(studentId);
        List<GoalResponse> responses = new ArrayList<>();
        for(Goal goal : goals) {
            responses.add(toResponse(goal));
        }
        return responses;
    }
    public GoalResponse updateGoalStatus(Long id, UpdateGoalStatusRequest updateGoalStatusRequest) {
        Goal goal = goalRepository.findById(id).orElseThrow(() -> new GoalNotFoundException("Goal not found"));
        goal.setStatus(updateGoalStatusRequest.getStatus());
        return toResponse(goalRepository.save(goal));
    }

}
