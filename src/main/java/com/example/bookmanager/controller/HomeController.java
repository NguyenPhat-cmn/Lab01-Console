package com.example.bookmanager.controller;

import com.example.bookmanager.model.Course;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.CourseRepository;
import com.example.bookmanager.repository.StudentRepository;
import com.example.bookmanager.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page, Authentication authentication, Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());

        // Nếu là student, load enrolled courses
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"))) {
            String username = authentication.getName();
            Student student = studentRepository.findByUsername(username).orElse(null);
            if (student != null) {
                model.addAttribute("enrolledCourseIds", enrollmentRepository.findByStudent(student)
                        .stream().map(e -> e.getCourse().getId()).toList());
            }
        }

        return "home";
    }

    @GetMapping("/courses/search")
    public String searchCourses(@RequestParam String name, Authentication authentication, Model model) {
        model.addAttribute("courses", courseRepository.findByNameContainingIgnoreCase(name));
        model.addAttribute("searchTerm", name);

        // Tương tự cho search
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"))) {
            String username = authentication.getName();
            Student student = studentRepository.findByUsername(username).orElse(null);
            if (student != null) {
                model.addAttribute("enrolledCourseIds", enrollmentRepository.findByStudent(student)
                        .stream().map(e -> e.getCourse().getId()).toList());
            }
        }

        return "search-results";
    }
}