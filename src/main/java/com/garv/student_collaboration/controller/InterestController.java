package com.garv.student_collaboration.controller;

import com.garv.student_collaboration.dto.InterestRequest;
import com.garv.student_collaboration.dto.InterestResponse;
import com.garv.student_collaboration.entity.Interest;
import com.garv.student_collaboration.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Interests",
        description = "Browse and manage the available interest catalogue"
)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;
    @Operation(
            summary = "Create an interest",
            description = "Adds an interest to the platform interest catalogue."
    )
    @PostMapping("/interests")
    @ResponseStatus(HttpStatus.CREATED)
    public InterestResponse createInterest(@Valid @RequestBody InterestRequest interestRequest){
        return interestService.createInterest(interestRequest);
    }
    @Operation(
            summary = "Get an interest",
            description = "Returns one interest by its ID."
    )
    @GetMapping("/interests/{id}")
    public InterestResponse getInterestById(@PathVariable Long id){
        return interestService.getInterestById(id);
    }
    @Operation(
            summary = "List interests",
            description = "Returns all interests available on the platform."
    )
    @GetMapping("/interests")
    public List<InterestResponse> getAllInterests(){
        return interestService.getAllInterests();
    }
}
