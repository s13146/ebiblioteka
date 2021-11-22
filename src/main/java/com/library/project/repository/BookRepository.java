package com.library.project.repository;

import com.library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE CONCAT(b.title, ' ', b.author, ' ', b.category) LIKE %?1%")
    public List<Book> getBookByTitleAuthorCategory(String keyword);
}
