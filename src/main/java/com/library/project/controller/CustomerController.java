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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private UserService userService;
    private BookRepository bookRepository;
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;

    public CustomerController(UserService userService, BookRepository bookRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list_user_books")
    public String viewMyBooksList(Model model) {
        UserEntity loggedUser = userService.getCurrentUser();
        List<Reservation> mybooks = reservationRepository.getMyReservation(loggedUser.getEmail());
        model.addAttribute("mybooks", mybooks);
        return "customer/list_user_books";
    }

    @RequestMapping("/book_reservation/{id}")
    public String bookReservation(@PathVariable(name = "id") long id) {
        UserEntity userEntity = userService.getCurrentUser();
        Book book = bookRepository.getById(id);
        if (userEntity.getReservationsCount() > 2)
            throw new ApiRequestException("Użytkownik ma za dużo wypożyczonych książek");
        else if (book.getBookStatus() == BookStatus.NIEDOSTEPNA)
            throw new ApiRequestException("Książka jest niedostępna");
        else {
            userService.addReservation(userEntity, book);
            return "customer/process_book_reservation";
        }
    }


    @GetMapping("/customer_details")
    public String viewCustomerDetails(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "customer/customer_details";
    }
}
