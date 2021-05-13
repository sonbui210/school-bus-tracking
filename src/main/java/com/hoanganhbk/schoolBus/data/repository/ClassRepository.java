package com.hoanganhbk.schoolBus.data.repository;

import com.hoanganhbk.schoolBus.data.model.ClassModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassModel, Long> {
}
