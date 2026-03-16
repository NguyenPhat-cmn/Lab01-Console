package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    private static final String UPLOAD_DIR = "uploads/images/";

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String list(Model model) {
        List<Book> books = repository.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@ModelAttribute Book book, @RequestParam("imageFile") MultipartFile file) {
        if (book != null) {
            if (!file.isEmpty()) {
                try {
                    // Tạo thư mục nếu chưa có
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // Tạo tên file unique
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath);

                    // Lưu đường dẫn vào book
                    book.setImageUrl("/" + UPLOAD_DIR + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Có thể thêm error handling
                }
            }
            repository.save(book);
        }
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        if (id != null) {
            Optional<Book> opt = repository.findById(id);
            if (opt.isPresent()) {
                model.addAttribute("book", opt.get());
                return "form";
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
        return "redirect:/books";
    }
}