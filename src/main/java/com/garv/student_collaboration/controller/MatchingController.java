package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.MatchResponse;
import com.garv.student_collaboration.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Student Matching",
        description = "Find collaboration opportunities that match a student's profile"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    @Operation(
            summary = "Find matches for a student",
            description = "Returns collaboration matches for a student based on their skills and interests."
    )
    @GetMapping("/students/{studentId}/matches")
    public List<MatchResponse> getMatchesForStudent(
            @PathVariable("studentId") Long studentId) {

        return matchingService.getMatchesForStudent(studentId);
    }
}
