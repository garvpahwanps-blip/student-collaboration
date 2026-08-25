package com.garv.student_collaboration.repository;
import com.garv.student_collaboration.entity.StudentInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInterestRepository extends JpaRepository<StudentInterest,Long> {
    boolean existsByStudent_IdAndInterest_Id(Long studentId,Long interestId);
}
