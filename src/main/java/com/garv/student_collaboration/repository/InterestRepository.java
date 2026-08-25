package com.garv.student_collaboration.repository;

import com.garv.student_collaboration.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest,Long> {
    Optional<Interest> findByName(String name);
}
