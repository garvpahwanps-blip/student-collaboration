package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.RequiredSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequiredSkillRepository extends JpaRepository<RequiredSkill,Long> {
   List<RequiredSkill> findAllByCollaborationRequest_Id(Long collaboration_request_id);
}
