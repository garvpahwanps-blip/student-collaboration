package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.MatchResponse;
import com.garv.student_collaboration.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/students/{studentId}/matches")
    public List<MatchResponse> getMatchesForStudent(
            @PathVariable("studentId") Long studentId) {

        return matchingService.getMatchesForStudent(studentId);
    }
}
