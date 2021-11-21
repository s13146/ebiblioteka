package com.library.project.service;

import com.library.project.model.Book;
import com.library.project.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public Book save(Book book){
        return bookRepository.save(book);
    }
}
