package com.example.BAI2.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Xin chào các bạn - Spring Boot chạy OK!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot 👋";
    }
}
