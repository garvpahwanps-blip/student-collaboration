package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.InterestRequest;
import com.garv.student_collaboration.dto.InterestResponse;
import com.garv.student_collaboration.entity.Interest;
import com.garv.student_collaboration.service.InterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;
    @PostMapping("/interests")
    @ResponseStatus(HttpStatus.CREATED)
    public InterestResponse createInterest(@Valid @RequestBody InterestRequest interestRequest){
        return interestService.createInterest(interestRequest);
    }
    @GetMapping("/interests/{id}")
    public InterestResponse getInterestById(@PathVariable Long id){
        return interestService.getInterestById(id);
    }
    @GetMapping("/interests")
    public List<InterestResponse> getAllInterests(){
        return interestService.getAllInterests();
    }
}
