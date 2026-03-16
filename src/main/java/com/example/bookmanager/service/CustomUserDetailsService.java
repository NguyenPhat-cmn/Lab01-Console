package com.example.bookmanager.service;

import com.example.bookmanager.model.Role;
import com.example.bookmanager.model.User;
import com.example.bookmanager.model.Student;
import com.example.bookmanager.repository.UserRepository;
import com.example.bookmanager.repository.StudentRepository;
import com.example.bookmanager.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // First, try to find in User table
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                        System.out.println("🔍 Loading user: " + username);
                        System.out.println("📧 Email: " + user.getEmail());
                        System.out.println("🔒 Enabled: " + user.isEnabled());
                        System.out.println("👥 Roles: " + user.getRoles().stream().map(Role::getName).toList());

                        return org.springframework.security.core.userdetails.User.builder()
                                        .username(user.getUsername())
                                        .password(user.getPassword())
                                        .authorities(user.getRoles().stream()
                                                        .map(role -> new SimpleGrantedAuthority(
                                                                        "ROLE_" + role.getName()))
                                                        .collect(Collectors.toList()))
                                        .accountLocked(!user.isEnabled())
                                        .build();
                }

                // If not found in User, try Student
                Student student = studentRepository.findByUsername(username).orElse(null);
                if (student != null) {
                        System.out.println("🔍 Loading student: " + username);
                        System.out.println("📧 Email: " + student.getEmail());

                        // Get STUDENT role
                        Role studentRole = roleRepository.findByName("STUDENT")
                                        .orElseThrow(() -> new RuntimeException("STUDENT role not found"));

                        Set<Role> roles = new HashSet<>();
                        roles.add(studentRole);

                        return org.springframework.security.core.userdetails.User.builder()
                                        .username(student.getUsername())
                                        .password(student.getPassword())
                                        .authorities(roles.stream()
                                                        .map(role -> new SimpleGrantedAuthority(
                                                                        "ROLE_" + role.getName()))
                                                        .collect(Collectors.toList()))
                                        .build();
                }

                throw new UsernameNotFoundException("User not found with username: " + username);
        }
}
