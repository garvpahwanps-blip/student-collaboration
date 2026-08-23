package com.garv.student_collaboration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.Locale;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Skill {
    public enum Category {
        PROGRAMMING_LANGUAGE, FRAMEWORK, DATABASE
        ,TOOL,CLOUD,SOFT_SKILL ,OTHER;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Enter the skill name")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @PrePersist
    @PreUpdate
    public void formatData() {
        if (name != null) {
            name = name
                    .replaceAll("\\s+", " ")
                    .trim()
                    .toLowerCase(Locale.ROOT);
        }
    }
    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    private Category category;

}
