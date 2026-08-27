package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.SkillRequest;
import com.garv.student_collaboration.dto.SkillResponse;
import com.garv.student_collaboration.entity.Skill;
import com.garv.student_collaboration.exception.DuplicateSkillException;
import com.garv.student_collaboration.exception.SkillNotFoundException;
import com.garv.student_collaboration.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillResponse toResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }
    public Skill toEntity(SkillRequest skillRequest) {
        return Skill.builder()
                .name(skillRequest.getName())
                .category(skillRequest.getCategory())
                .build();
    }
    public SkillResponse createSkill(SkillRequest skillRequest) {
        String normalizedName = skillRequest.getName().toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
        Optional<Skill> existingSkill = skillRepository.findByName(normalizedName);
        if (existingSkill.isPresent()) {
            throw new DuplicateSkillException(normalizedName + " already exists");
        }
        Skill skill = toEntity(skillRequest);
        skill.setName(normalizedName);
        Skill savedSkill = skillRepository.save(skill);
        return toResponse(savedSkill);
    }
    public SkillResponse getSkillById(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(()-> new SkillNotFoundException("Skill not found"));
        return toResponse(skill);
    }
    public List<SkillResponse> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        List<SkillResponse> skillResponses = new ArrayList<>();
        skills.forEach(skill -> skillResponses.add(toResponse(skill)));
        return skillResponses;
    }


}
