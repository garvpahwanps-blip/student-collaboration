package com.garv.student_collaboration.repository;
import com.garv.student_collaboration.entity.StudentInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentInterestRepository extends JpaRepository<StudentInterest,Long> {
    boolean existsByStudent_IdAndInterest_Id(Long studentId,Long interestId);
    List<StudentInterest> findAllByStudent_Id(Long studentId);
}
