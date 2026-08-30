package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.MatchResponse;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.RequiredSkill;
import com.garv.student_collaboration.entity.StudentSkill;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.RequiredSkillRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import com.garv.student_collaboration.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final StudentRepository studentRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final RequiredSkillRepository requiredSkillRepository;
    public List<MatchResponse> getMatchesForStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException("Student not found");
        }
        List<StudentSkill> studentSkillsList = studentSkillRepository.findAllByStudent_Id(studentId);
        Map<String,StudentSkill.Level> studentSkills = new HashMap<>();
        List<MatchResponse> matches = new ArrayList<>();
        for(StudentSkill studentSkill : studentSkillsList) {
            studentSkills.put(studentSkill.getSkill().getName(),studentSkill.getLevel());
        }

        List<CollaborationRequest> openRequests = collaborationRequestRepository.findAllByStatus(CollaborationRequest.Status.OPEN);
        for(CollaborationRequest openRequest : openRequests) {
            if(openRequest.getCreatedBy().getId().equals(studentId)) {
                continue;
            }
            List<String> matchingSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();
            List<RequiredSkill> requiredSkills = requiredSkillRepository.findAllByCollaborationRequest_Id(openRequest.getId());
            int matchedPoints=0;
            int totalPoints =0;
            for(RequiredSkill requiredSkill : requiredSkills) {
                String requiredSkillName = requiredSkill.getSkill().getName();
                int importancePoints = getImportancePoints(requiredSkill.getImportance());
                totalPoints += importancePoints;
                StudentSkill.Level studentLevel = studentSkills.get(requiredSkillName);
                if(studentLevel!=null && getStudentLevelRank(studentLevel)>=getRequiredLevelRank(requiredSkill.getDesiredLevel())){
                    matchingSkills.add(requiredSkillName);
                    matchedPoints += importancePoints;
                }
                else{
                    missingSkills.add(requiredSkillName);
                }
            }
            double score = (totalPoints==0)?0:(double) matchedPoints/totalPoints *100;
            matches.add(MatchResponse.builder()
                    .collaborationRequestId(openRequest.getId())
                    .title(openRequest.getTitle())
                    .score(score)
                    .matchingSkills(matchingSkills)
                    .missingSkills(missingSkills)
                    .build());
        }
        matches.sort(Comparator.comparing(MatchResponse::getScore).reversed());
        return matches;


    }
    private int getStudentLevelRank(StudentSkill.Level studentLevel) {
        return switch (studentLevel){
            case BEGINNER -> 1;
            case INTERMEDIATE -> 2;
            case ADVANCED -> 3;
        };
    }
    private  int getRequiredLevelRank(RequiredSkill.DesiredLevel skillLevel) {
        return switch(skillLevel){
            case BEGINNER -> 1;
            case INTERMEDIATE -> 2;
            case ADVANCED -> 3;
        };
    }
    private int getImportancePoints(RequiredSkill.Importance importance){
        return switch (importance) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
        };
    }
}
