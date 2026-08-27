package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.InterestRequest;
import com.garv.student_collaboration.dto.InterestResponse;
import com.garv.student_collaboration.entity.Interest;
import com.garv.student_collaboration.exception.DuplicateInterestException;
import com.garv.student_collaboration.exception.InterestNotFoundException;
import com.garv.student_collaboration.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;
    private InterestResponse toResponse(Interest interest) {
        return InterestResponse.builder()
                .id(interest.getId())
                .name(interest.getName())
                .build();
    }
    private Interest toEntity(InterestRequest interestRequest) {
        return Interest.builder()
                .name(interestRequest.getName())
                .build();
    }
    public InterestResponse createInterest(InterestRequest interestRequest) {
        String normalizedInterestName = interestRequest.getName().trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if(interestRepository.findByName(normalizedInterestName).isPresent()){
            throw new DuplicateInterestException(normalizedInterestName + " already exists");
        }
        interestRequest.setName(normalizedInterestName);
        Interest interest = toEntity(interestRequest);
        Interest savedInterest = interestRepository.save(interest);
        return toResponse(savedInterest);
    }
    public InterestResponse getInterestById(Long id){
        Interest interest = interestRepository.findById(id).orElseThrow(()->new InterestNotFoundException("Interest not found"));
        return toResponse(interest);
    }
    public List<InterestResponse> getAllInterests(){
        List<Interest> interests = interestRepository.findAll();
        List<InterestResponse> interestResponses = new ArrayList<>();
        for(Interest interest : interests){
            interestResponses.add(toResponse(interest));
        }
        return interestResponses;
    }
}
