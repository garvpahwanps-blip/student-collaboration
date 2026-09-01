package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {
    public enum YearOfStudy{
        FIRST,SECOND,THIRD,FOURTH;
    }
    public enum Gender{
        MALE,FEMALE,OTHER;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name can not be blank")
    private String fullName;
    @NotBlank(message = "email can not be blank")
    @Email(message = "please enter a valid email")
    @Column(unique = true, nullable = false,length = 255)
    private String email;
    @PrePersist
    @PreUpdate
    public void formatData() {
        if (email != null) {
            email = email.trim().toLowerCase(Locale.ROOT);
        }
    }
    private String university;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String profilePicture;
    @NotNull(message = "year of Study is required")
    @Enumerated(EnumType.STRING)
    private YearOfStudy yearOfStudy;
    @Column(nullable = false,length = 100)
    private String passwordHash;


}
