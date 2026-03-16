package com.example.bookmanager.controller;

import com.example.bookmanager.model.Enrollment;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.model.Course;
import com.example.bookmanager.repository.EnrollmentRepository;
import com.example.bookmanager.repository.StudentRepository;
import com.example.bookmanager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enroll")
@PreAuthorize("hasRole('STUDENT')")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/{courseId}")
    public String enroll(@PathVariable Long courseId, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            redirectAttributes.addFlashAttribute("error", "Already enrolled in this course!");
        } else {
            Enrollment enrollment = new Enrollment(student, course);
            enrollmentRepository.save(enrollment);
            redirectAttributes.addFlashAttribute("message", "Enrolled successfully!");
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("enrollments", enrollmentRepository.findByStudent(student));
        return "my-courses";
    }
}