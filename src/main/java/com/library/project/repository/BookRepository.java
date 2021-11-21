package com.library.project.repository;

import com.library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b from Book b where b.title like %:title%")
    List<Book> getBookByTitle(String title);

    @Query("SELECT b from Book b where b.author like %:author%")
    List<Book> getBookByAuthor(String author);

    @Query("SELECT b from Book b where b.category = :category")
    List<Book> getBookByCategory(String category);
}
