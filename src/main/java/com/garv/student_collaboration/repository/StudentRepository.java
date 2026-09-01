package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
}
