package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.SkillSuggestionResponse;
import com.garv.student_collaboration.entity.CollaborationRequest;
import com.garv.student_collaboration.entity.RequiredSkill;
import com.garv.student_collaboration.entity.Skill;
import com.garv.student_collaboration.exception.CollaborationRequestNotFoundException;
import com.garv.student_collaboration.repository.CollaborationRequestRepository;
import com.garv.student_collaboration.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillSuggestionService {
    private final CollaborationRequestRepository collaborationRequestRepository;
    private final SkillRepository skillRepository;
    private final OllamaClientService ollamaClientService;
    private final ObjectMapper objectMapper;

    public List<SkillSuggestionResponse> suggestSkillsForRequest(Long collaborationRequestId) {
        CollaborationRequest collaborationRequest = collaborationRequestRepository.findById(collaborationRequestId).orElseThrow(()->new CollaborationRequestNotFoundException("CollaborationRequest not found"));
        List<Skill> skills = skillRepository.findAll();
        List<String> allowedSkills=new ArrayList<>();
        for (Skill skill : skills) {
            allowedSkills.add(skill.getName());
        }
        String title = collaborationRequest.getTitle();
        String description = collaborationRequest.getDescription();
        String allowedSkillsText = String.join(", ", allowedSkills);

        String prompt = """
        Analyze this collaboration request and suggest the required technical skills.

        Title:
        %s

        Description:
        %s

        Allowed skills:
        %s

        Only suggest skills from the allowed skills list.
        Do not invent new skill names.

        Return only a valid JSON array of objects.
        The response must start with [ and end with ].

        Each object must use exactly these fields:
        - skillName
        - desiredLevel: BEGINNER, INTERMEDIATE, or ADVANCED
        - importance: LOW, MEDIUM, or HIGH
        - reason

        Example format:
        [
          {
            "skillName": "java",
            "desiredLevel": "INTERMEDIATE",
            "importance": "HIGH",
            "reason": "The project requires backend development."
          }
        ]

        Do not include Markdown.
        Do not include ```json.
        Do not include any explanation outside the JSON array.
        """.formatted(title, description, allowedSkillsText);

        String rawResponse= ollamaClientService.askOllama(prompt);
        try {
            List<SkillSuggestionResponse> suggestions =
                    objectMapper.readValue(
                            rawResponse,
                            new TypeReference<List<SkillSuggestionResponse>>() {
                            }
                    );

            return validateSuggestions(suggestions, skills);

        } catch (JacksonException exception) {
            throw new IllegalStateException("Ollama returned invalid JSON", exception);
        }
    }

    private List<SkillSuggestionResponse> validateSuggestions(List<SkillSuggestionResponse> suggestions, List<Skill> skills) {
        Map<String, Skill> skillsByName = new HashMap<>();
        for (Skill skill : skills) {
            String normalizedName = normalize(skill.getName());
            skillsByName.put(normalizedName, skill);
        }
        Set<String> alreadySeenSkills = new HashSet<>();
        List<SkillSuggestionResponse> validSuggestions = new ArrayList<>();
        for (SkillSuggestionResponse suggestion : suggestions) {
            if (suggestion == null) {
                continue;
            }
            String normalizedSkillName = normalize(suggestion.getSkillName());
            if (normalizedSkillName.isBlank()) {
                log.warn("Ignoring suggestion with empty skill name");
                continue;
            }
            Skill databaseSkill = skillsByName.get(normalizedSkillName);
            if (databaseSkill == null) {
                log.warn("Ignoring unknown skill suggested by AI: {}", suggestion.getSkillName());
                continue;
            }

            if (!alreadySeenSkills.add(normalizedSkillName)) {
                log.warn("Ignoring duplicate skill suggested by AI: {}", suggestion.getSkillName());
                continue;
            }
            String desiredLevel = normalize(suggestion.getDesiredLevel()).toUpperCase(Locale.ROOT);
            String importance = normalize(suggestion.getImportance()).toUpperCase(Locale.ROOT);
            if (!isValidDesiredLevel(desiredLevel)) {
                log.warn("Ignoring skill with invalid desired level: {}", desiredLevel);
                continue;
            }
            if (!isValidImportance(importance)) {
                log.warn("Ignoring skill with invalid importance: {}", importance
                );
                continue;
            }
            validSuggestions.add(SkillSuggestionResponse.builder()
                            .skillId(databaseSkill.getId())
                            .skillName(databaseSkill.getName())
                            .desiredLevel(desiredLevel)
                            .importance(importance)
                            .reason(suggestion.getReason())
                            .build()
            );
        }

        return validSuggestions;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isValidDesiredLevel(String value) {
        try {
            RequiredSkill.DesiredLevel.valueOf(value);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean isValidImportance(String value) {
        try {
            RequiredSkill.Importance.valueOf(value);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }




}


