package com.example.bookmanager.controller;

import com.example.bookmanager.model.Book;
import com.example.bookmanager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

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
    public String save(@ModelAttribute Book book) {
        if (book != null) {
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