package com.library.project;

import com.library.project.model.Book;
import com.library.project.model.enums.BookStatus;
import com.library.project.model.enums.Category;
import com.library.project.repository.BookRepository;
import com.library.project.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookServiceTests {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldSaveBook() {
        Book book = new Book();
        book.setTitle("Pan Tadeusz");
        book.setAuthor("Adam Mickiewicz");
        book.setCategory(Category.LEKTURA);

        List<Book> all = bookRepository.findAll();
        assertThat(all).isNotEmpty();
    }
}
