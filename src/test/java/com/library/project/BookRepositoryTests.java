package com.library.project;

import com.library.project.model.Book;
import com.library.project.model.Category;
import com.library.project.model.UserEntity;
import com.library.project.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void TestCreateBook(){
        Book book = new Book();
        book.setTitle("Pan Tadeusz");
        book.setAuthor("Adam Mickiewicz");
        book.setCategory(Category.LEKTURA);
        book.setIsAvailable((byte) 1);

        Book savedBook = bookRepository.save(book);
        Book existsBook = testEntityManager.find(Book.class,savedBook.getId());

        assertThat(existsBook.getAuthor()).isEqualTo(book.getAuthor());

    }
}
