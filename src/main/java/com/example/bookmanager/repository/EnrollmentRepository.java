package com.example.bookmanager.repository;

import com.example.bookmanager.model.Enrollment;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);

    boolean existsByStudentAndCourse(Student student, Course course);
}