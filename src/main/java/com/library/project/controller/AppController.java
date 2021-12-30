package com.library.project.controller;

import com.library.project.model.Book;
import com.library.project.repository.BookRepository;
import com.library.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    private UserService userService;
    private BookRepository bookRepository;

    public AppController(UserService userService, BookRepository bookRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("")
    public String viewHomePageWithBooksList(Model model) {
        List<Book> listBooks = bookRepository.findAll();
        model.addAttribute("listBooks", listBooks);
        return "index";
    }


    @GetMapping("/console")
    public String viewConsole() {
        return "console";
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @GetMapping("/customer")
    public String viewCustomerPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "customer";
    }

    @GetMapping("/search_book")
    public String viewSearchBook() {
        return "search_book";
    }

    @GetMapping("/search_book_unlogged")
    public String viewSearchBookUnlogged() {
        return "search_book_unlogged";
    }

    @PostMapping("/search_result")
    public String viewSearchResult(Model model, @RequestParam String name) {
        List<Book> bookList = bookRepository.getBookByTitleAuthorCategory(name);
        model.addAttribute("bookList", bookList);
        return "search_result";
    }

    @GetMapping("/list_books")
    public String viewBooksList(Model model) {
        List<Book> listBooks = bookRepository.findAll();
        model.addAttribute("listBooks", listBooks);
        return "list_books";
    }

    @GetMapping("/error")
    public String anyError() {
        return "error";
    }
}
