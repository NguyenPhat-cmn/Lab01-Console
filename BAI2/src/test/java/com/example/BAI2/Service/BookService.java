package com.example.BAI2.Service;



import com.example.BAI2.Model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>();

    public BookService() {
        // Dữ liệu mẫu ban đầu
        books.add(new Book(1, "Java Basics", "NGUYEN TAN PHAT", "Programming", 200));
        books.add(new Book(2, "Spring Boot", "NGUYEN TAN PHAT", "Programming", 250));
        books.add(new Book(3, "Clean Code", "Robert Martin", "Software", 300));
        books.add(new Book(4, "Design Patterns", "GoF", "Software", 280));
        books.add(new Book(5, "Database System", "Nguyễn Văn B", "Database", 220));
    }

    // GET all
    public List<Book> getAllBooks() {
        return books;
    }

    // GET by id
    public Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    // POST - add
    public Book addBook(Book book) {
        books.add(book);
        return book;
    }

    public Book updateBook(int id, Book newBook) {
        for (Book b : books) {
            if (b.getId() == id) {
                b.setTitle(newBook.getTitle());
                b.setAuthor(newBook.getAuthor());
                b.setCategory(newBook.getCategory());
                b.setPrice(newBook.getPrice());
                return b;
            }
        }
        return null;
    }

    // DELETE
    public boolean deleteBook(int id) {
        return books.removeIf(b -> b.getId() == id);
    }
}


