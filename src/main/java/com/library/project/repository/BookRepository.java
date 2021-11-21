package com.library.project.repository;

import com.library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b from Book b where b.title = :title")
    Book getBookByTitle(String title);

    @Query("SELECT b from Book b where b.author = :author")
    Book getBookByAuthor(String author);

    @Query("SELECT b from Book b where b.category = :category")
    Book getBookByCategory(String category);
}
