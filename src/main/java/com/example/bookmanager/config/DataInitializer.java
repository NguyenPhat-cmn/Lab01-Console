package com.example.bookmanager.config;

import com.example.bookmanager.model.Role;
import com.example.bookmanager.model.User;
import com.example.bookmanager.model.Course;
import com.example.bookmanager.repository.RoleRepository;
import com.example.bookmanager.repository.UserRepository;
import com.example.bookmanager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = new Role("USER");
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role("ADMIN");
                    return roleRepository.save(role);
                });

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseGet(() -> {
                    Role role = new Role("STUDENT");
                    return roleRepository.save(role);
                });

        // Initialize test users
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setEmail("user@example.com");
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            System.out.println("✅ Tài khoản USER đã được tạo: user / password123");
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("✅ Tài khoản ADMIN đã được tạo: admin / admin123");
        }

        // Initialize sample courses
        if (courseRepository.count() == 0) {
            courseRepository.save(new Course("Java Programming", 3, "Dr. Smith", "/images/java.jpg"));
            courseRepository.save(new Course("Data Structures", 4, "Prof. Johnson", "/images/ds.jpg"));
            courseRepository.save(new Course("Web Development", 3, "Ms. Lee", "/images/web.jpg"));
            courseRepository.save(new Course("Database Systems", 3, "Dr. Brown", "/images/db.jpg"));
            courseRepository.save(new Course("Algorithms", 4, "Prof. Davis", "/images/algo.jpg"));
            courseRepository.save(new Course("Machine Learning", 3, "Dr. Wilson", "/images/ml.jpg"));
            System.out.println("✅ Đã tạo các học phần mẫu");
        }
    }
}
