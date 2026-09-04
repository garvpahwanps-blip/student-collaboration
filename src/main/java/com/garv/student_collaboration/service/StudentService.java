package com.garv.student_collaboration.service;

import com.garv.student_collaboration.dto.StudentResponse;
import com.garv.student_collaboration.dto.UpdateStudentRequest;
import com.garv.student_collaboration.entity.Student;
import com.garv.student_collaboration.exception.StudentNotFoundException;
import com.garv.student_collaboration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
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
    public StudentResponse updateStudent(Long studentId, UpdateStudentRequest updateStudentRequest){
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new StudentNotFoundException("Student not found"));
        if(updateStudentRequest.getFullName() != null){
            student.setFullName(updateStudentRequest.getFullName());
        }
        if(updateStudentRequest.getGender() != null){
            student.setGender(updateStudentRequest.getGender());
        }
        if(updateStudentRequest.getBirthDate() != null){
            student.setBirthDate(updateStudentRequest.getBirthDate());
        }
        if(updateStudentRequest.getProfilePicture() != null){
            student.setProfilePicture(updateStudentRequest.getProfilePicture());
        }
        if(updateStudentRequest.getUniversity() != null){
            student.setUniversity(updateStudentRequest.getUniversity());
        }
        if(updateStudentRequest.getYearOfStudy() != null){
            student.setYearOfStudy(updateStudentRequest.getYearOfStudy());
        }
        Student updatedStudent = studentRepository.save(student);
        return toStudentResponse(updatedStudent);
    }

}
