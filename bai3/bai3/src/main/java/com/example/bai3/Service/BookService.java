package com.example.bai3.Service;

import com.example.bai3.Model.Book;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>();
    private Long nextId = 1L;

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book book) {
        book.setId(nextId++);
        books.add(book);
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public void updateBook(Book updated) {
        books.stream()
                .filter(b -> b.getId().equals(updated.getId()))
                .findFirst()
                .ifPresent(b -> {
                    b.setTitle(updated.getTitle());
                    b.setAuthor(updated.getAuthor());
                });
    }

    public void deleteBook(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}
