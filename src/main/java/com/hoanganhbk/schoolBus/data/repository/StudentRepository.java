package com.hoanganhbk.schoolBus.data.repository;

import com.hoanganhbk.schoolBus.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s where concat('',s.id) like concat('%', :searchKey, '%') or lower(s.fullName) like concat('%', lower(:searchKey), '%') ")
    Iterable<Student> searchStudent(@Param("searchKey") String searchKey);
}
