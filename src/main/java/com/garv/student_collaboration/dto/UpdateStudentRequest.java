package com.garv.student_collaboration.dto;

import com.garv.student_collaboration.entity.Student;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {
    private String fullName;
    private String university;
    private LocalDate birthDate;
    private Student.Gender gender;
    private String profilePicture;
    private Student.YearOfStudy yearOfStudy;
}
