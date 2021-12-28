package com.library.project.controller;

import com.library.project.exception.ApiRequestException;
import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.BookStatus;
import com.library.project.repository.BookRepository;
import com.library.project.repository.ReservationRepository;
import com.library.project.repository.UserRepository;
import com.library.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    private UserService userService;
    private BookRepository bookRepository;
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;

    public AppController(UserService userService, BookRepository bookRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
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
        UserEntity loggedUser = userService.getCurrentUser();
        String fullname = loggedUser.getFirstName() + "  " + loggedUser.getLastName();
        model.addAttribute("fullname", fullname);
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

    @GetMapping("/list_user_books")
    public String viewMyBooksList(Model model) {
        UserEntity loggedUser = userService.getCurrentUser();
        String email = loggedUser.getEmail();
        List<Reservation> mybooks = reservationRepository.getMyReservation(email);
        model.addAttribute("mybooks", mybooks);
        return "customer/list_user_books";
    }

    @RequestMapping("/book_reservation/{id}")
    public String bookReservation(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userService.getCurrentUser();
        Book book = bookRepository.getById(id);
        if (userEntity.getReservationsCount() > 2)
            throw new ApiRequestException("Użytkownik ma za dużo wypożyczonych książek");
        else if (book.getBookStatus() == BookStatus.NIEDOSTEPNA)
            throw new ApiRequestException("Książka jest niedostępna");
        else {
            book.setBookStatus(BookStatus.NIEDOSTEPNA);
            model.addAttribute("book", book);
            userService.addReservation(userEntity, book);
            userEntity.setReservationsCount(userEntity.getReservationsCount() + 1);
            userRepository.save(userEntity);
            return "customer/process_book_reservation";
        }
    }


    @GetMapping("/customer_details")
    public String viewCustomerDetails(Model model) {
        UserEntity loggedUser = userService.getCurrentUser();
        String firstName = loggedUser.getFirstName();
        String lastName = loggedUser.getLastName();
        String email = loggedUser.getEmail();
        model.addAttribute("firstname", firstName);
        model.addAttribute("lastname", lastName);
        model.addAttribute("email", email);
        return "customer/customer_details";
    }

    @GetMapping("/error")
    public String anyError() {
        return "error";
    }
}
