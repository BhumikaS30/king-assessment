package com.king.assessment.repository;

import com.king.assessment.model.Level;
import com.king.assessment.model.LevelId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

    Level findByLevelId(LevelId levelId);
}
