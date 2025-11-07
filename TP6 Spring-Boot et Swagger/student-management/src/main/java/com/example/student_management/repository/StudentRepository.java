package com.example.student_management.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.student_management.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Custom query to count students grouped by year of birth
     * Returns a collection containing year and count
     */
    @Query("SELECT YEAR(s.dateNaissance) as year, COUNT(s) as count FROM Student s GROUP BY YEAR(s.dateNaissance)")
    Collection<Object[]> findNbrStudentByYear();
}