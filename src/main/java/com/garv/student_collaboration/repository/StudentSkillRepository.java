package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSkillRepository extends JpaRepository<StudentSkill,Long> {
    boolean existsByStudent_IdAndSkill_Id(Long studentId,Long skillId);
}
