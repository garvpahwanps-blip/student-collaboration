package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.RequiredSkillRequest;
import com.garv.student_collaboration.dto.RequiredSkillResponse;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.RequiredSkill;
import com.garv.student_collaboration.entity.Skill;
import com.garv.student_collaboration.exception.*;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.RequiredSkillRepository;
import com.garv.student_collaboration.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequiredSkillService {
    private final RequiredSkillRepository requiredSkillRepository;
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final SkillRepository skillRepository;
    private RequiredSkillResponse toResponse(RequiredSkill requiredSkill) {
        return RequiredSkillResponse.builder()
                .id(requiredSkill.getId())
                .collaborationRequestId(requiredSkill.getCollaborationRequest().getId())
                .skillId(requiredSkill.getSkill().getId())
                .skillName(requiredSkill.getSkill().getName())
                .desiredLevel(requiredSkill.getDesiredLevel())
                .importance(requiredSkill.getImportance())
                .build();
    }
    private RequiredSkill toEntity(RequiredSkillRequest requiredSkillRequest, CollaborationRequest collaborationRequest, Skill skill) {
        return RequiredSkill.builder()
                .collaborationRequest(collaborationRequest)
                .skill(skill)
                .desiredLevel(requiredSkillRequest.getDesiredLevel())
                .importance(requiredSkillRequest.getImportance())
                .build();
    }
    public RequiredSkillResponse createRequiredSkill(RequiredSkillRequest requiredSkillRequest,Long studentId) {
        CollaborationRequest collaborationRequest = collaborationRequestRepository.findById(requiredSkillRequest.getCollaborationRequestId()).orElseThrow(()->new CollaborationRequestNotFoundException("CollaborationRequest not found"));
        Skill skill = skillRepository.findById(requiredSkillRequest.getSkillId()).orElseThrow(()->new SkillNotFoundException("Required skill not found"));
        if(!collaborationRequest.getCreatedBy().getId().equals(studentId)) {
            throw new AuthorizationException("this collaboration request belongs to other user");
        }
        if(requiredSkillRepository.existsByCollaborationRequest_IdAndSkill_Id(collaborationRequest.getId(),skill.getId())){
            throw new DuplicateRequiredSkillException("This skill is already required for the collaboration request");
        }
        RequiredSkill savedRequiredSkill = requiredSkillRepository.save(toEntity(requiredSkillRequest, collaborationRequest, skill));
        return toResponse(savedRequiredSkill);
    }
    public RequiredSkillResponse getRequiredSkillById(Long requiredSkillId) {
        RequiredSkill requiredSkill = requiredSkillRepository.findById(requiredSkillId).orElseThrow(()->new RequiredSkillNotFoundException("Required skill not found"));
        return toResponse(requiredSkill);
    }
    public List<RequiredSkillResponse> getRequiredSkillsByCollaborationRequestId(Long collaborationRequestId) {
        if(!collaborationRequestRepository.existsById(collaborationRequestId)) {
            throw new CollaborationRequestNotFoundException("CollaborationRequest not found");
        }
        List<RequiredSkill> requiredSkills =requiredSkillRepository.findAllByCollaborationRequest_Id(collaborationRequestId);
        List<RequiredSkillResponse> requiredSkillResponses = new ArrayList<>();
        for(RequiredSkill requiredSkill : requiredSkills){
            requiredSkillResponses.add(toResponse(requiredSkill));
        }
        return requiredSkillResponses;
    }
    public void deleteRequiredSkillById(Long requiredSkillId,Long studentId) {
        RequiredSkill requiredSkill = requiredSkillRepository.findById(requiredSkillId).orElseThrow(()->new RequiredSkillNotFoundException("Required skill not found"));
        if(!requiredSkill.getCollaborationRequest().getCreatedBy().getId().equals(studentId)) {
            throw new AuthorizationException("this collaboration request belongs to other user");
        }
        requiredSkillRepository.delete(requiredSkill);
    }
    @Transactional
    public List<RequiredSkillResponse> createRequiredSkills(List<RequiredSkillRequest> requests,Long studentId) {
        List<RequiredSkillResponse> responses = new ArrayList<>();
        for(RequiredSkillRequest request : requests){
            RequiredSkillResponse response = createRequiredSkill(request,studentId);
            responses.add(response);
        }
        return responses;
    }

}
