package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.CreateStudentRequest;
import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.DuplicateEmailException;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private Student toEntity(CreateStudentRequest createStudentRequest) {
        return Student.builder()
                .fullName(createStudentRequest.getFullName())
                .email(createStudentRequest.getEmail())
                .gender(createStudentRequest.getGender())
                .birthDate(createStudentRequest.getBirthDate())
                .profilePicture(createStudentRequest.getProfilePicture())
                .university(createStudentRequest.getUniversity())
                .yearOfStudy(createStudentRequest.getYearOfStudy())
                .build();
    }
    private StudentResponse toStudentResponse(Student student){
        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .gender(student.getGender())
                .birthDate(student.getBirthDate())
                .profilePicture(student.getProfilePicture())
                .university(student.getUniversity())
                .yearOfStudy(student.getYearOfStudy())
                .build();
    }
    public StudentResponse createStudent(CreateStudentRequest request){
        String normalizedEmail = request.getEmail().toLowerCase(Locale.ROOT).trim();
        if(studentRepository.existsByEmail(normalizedEmail)){
            throw new DuplicateEmailException(normalizedEmail+" already exists");
        }
        Student student = toEntity(request);
        student.setEmail(normalizedEmail);
        Student savedStudent = studentRepository.save(student);
        return toStudentResponse(savedStudent);
    }
    public StudentResponse getStudentById(Long id){
        Student student= studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(id+" not found"));
        return toStudentResponse(student);

    }
    public List<StudentResponse> getAllStudents(){
        List<Student> students = studentRepository.findAll();
        List<StudentResponse> studentResponseList = new ArrayList<>();
        for(Student student : students){
            studentResponseList.add(toStudentResponse(student));
        }
        return studentResponseList;
    }

}
