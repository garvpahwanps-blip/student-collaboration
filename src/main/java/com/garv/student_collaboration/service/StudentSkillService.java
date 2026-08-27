package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.StudentSkillRequest;
import com.garv.student_collaboration.dto.StudentSkillResponse;
import com.garv.student_collaboration.entity.Skill;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.entity.StudentSkill;
import com.garv.student_collaboration.exception.DuplicateStudentSkillException;
import com.garv.student_collaboration.exception.SkillNotFoundException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.SkillRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import com.garv.student_collaboration.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentSkillService {
    private  final StudentSkillRepository studentSkillRepository;
    private  final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private StudentSkillResponse toResponse(StudentSkill studentSkill){
        return StudentSkillResponse.builder()
                .id(studentSkill.getId())
                .learning(studentSkill.isLearning())
                .level(studentSkill.getLevel())
                .studentId(studentSkill.getStudent().getId())
                .skillId(studentSkill.getSkill().getId())
                .skillName(studentSkill.getSkill().getName())
                .build();
    }
    private StudentSkill toEntity(StudentSkillRequest studentSkillRequest,Student student,
                                 Skill skill) {

        return StudentSkill.builder()
                .student(student)
                .skill(skill)
                .level(studentSkillRequest.getLevel())
                .learning(Boolean.TRUE.equals(studentSkillRequest.getLearning()))
                .build();

    }
    public StudentSkillResponse createStudentSkill(StudentSkillRequest studentSkillRequest) {
        Student student = studentRepository.findById(studentSkillRequest.getStudentId()).orElseThrow(()->new StudentNotFoundException("Student not found"));
        Skill skill = skillRepository.findById(studentSkillRequest.getSkillId()).orElseThrow(()->new SkillNotFoundException("Skill not found"));
        if(studentSkillRepository.existsByStudent_IdAndSkill_Id(studentSkillRequest.getStudentId(),studentSkillRequest.getSkillId())) {
            throw new DuplicateStudentSkillException("Student already has this skill");
        }
        StudentSkill savedStudentSkill =studentSkillRepository.save(toEntity(studentSkillRequest,student,skill));
        return toResponse(savedStudentSkill);
    }
    public List<StudentSkillResponse> getAllStudentSkills(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found");
        }
        List<StudentSkill> studentSkills = studentSkillRepository.findAllByStudent_Id(studentId);
        List<StudentSkillResponse> studentSkillResponses = new ArrayList<>();
        for (StudentSkill studentSkill : studentSkills) {
            studentSkillResponses.add(toResponse(studentSkill));
        }
        return studentSkillResponses;
    }


}
