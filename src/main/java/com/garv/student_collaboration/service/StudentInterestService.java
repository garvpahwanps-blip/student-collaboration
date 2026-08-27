package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.InterestResponse;
import com.garv.student_collaboration.dto.StudentInterestRequest;
import com.garv.student_collaboration.dto.StudentInterestResponse;
import com.garv.student_collaboration.entity.Interest;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.entity.StudentInterest;
import com.garv.student_collaboration.exception.DuplicateStudentInterestException;
import com.garv.student_collaboration.exception.InterestNotFoundException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.InterestRepository;
import com.garv.student_collaboration.repository.StudentInterestRepository;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentInterestService {
    private final StudentInterestRepository studentInterestRepository;
    private final StudentRepository studentRepository;
    private final InterestRepository interestRepository;
    private StudentInterestResponse toResponse(StudentInterest  studentInterest) {
        return StudentInterestResponse.builder()
                .id(studentInterest.getId())
                .studentId(studentInterest.getStudent().getId())
                .interestId(studentInterest.getInterest().getId())
                .interestName(studentInterest.getInterest().getName())
                .build();
    }
    private StudentInterest toEntity( Student student, Interest interest) {
        return StudentInterest.builder()
                .interest(interest)
                .student(student)
                .build();
    }
    public StudentInterestResponse createStudentInterest(StudentInterestRequest studentInterestRequest) {
        Student student = studentRepository.findById(studentInterestRequest.getStudentId()).orElseThrow(()->new StudentNotFoundException("Student not found"));
        Interest interest = interestRepository.findById(studentInterestRequest.getInterestId()).orElseThrow(()->new InterestNotFoundException("Interest not found"));
        if(studentInterestRepository.existsByStudent_IdAndInterest_Id(student.getId(), interest.getId())) {
            throw new DuplicateStudentInterestException("Student already has this interest");
        }
        StudentInterest studentInterest = studentInterestRepository.save(toEntity(student,interest));
        return toResponse(studentInterest);

    }
    public List<StudentInterestResponse> getAllInterestByStudentId(Long StudentId) {
        if(!studentRepository.existsById(StudentId)){
            throw new StudentNotFoundException("Student not found");
        }
        List<StudentInterest> studentInterests = studentInterestRepository.findAllByStudent_Id(StudentId);
        List<StudentInterestResponse> studentInterestResponses = new ArrayList<>();
        for (StudentInterest studentInterest : studentInterests) {
            studentInterestResponses.add(toResponse(studentInterest));
        }
        return studentInterestResponses;
    }
}
