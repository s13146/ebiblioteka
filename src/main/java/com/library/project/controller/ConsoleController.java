package com.library.project.controller;

import com.library.project.exception.ApiRequestException;
import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.ReservationStatus;
import com.library.project.repository.BookRepository;
import com.library.project.repository.ReservationRepository;
import com.library.project.repository.UserRepository;
import com.library.project.service.BookService;
import com.library.project.service.ReservationService;
import com.library.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/console")
public class ConsoleController {

    private UserService userService;
    private UserRepository userRepository;
    private BookService bookService;
    private BookRepository bookRepository;
    private ReservationService reservationService;
    private ReservationRepository reservationRepository;

    public ConsoleController(UserService userService, UserRepository userRepository, BookService bookService, BookRepository bookRepository, ReservationService reservationService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/registration")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "console/registration";
    }

    @PostMapping("/process_register")
    public String processRegistration(UserEntity userEntity) {
        try {
            userService.save(userEntity);
            return "console/process_register";
        } catch (Exception e) {
            throw new ApiRequestException("Wystąpił błąd podczas rejestrowania tego użytkwonika");
        }

    }

    @GetMapping("/add_book")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        return "console/add_book";
    }

    @PostMapping("/process_add_book")
    public String processAddBook(Book book) {
        bookService.save(book);
        return "console/process_add_book";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) {
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "console/list_users";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditUserEntityForm(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("console/edit_user");

        UserEntity userEntity = userRepository.getById(id);
        mav.addObject("userEntity", userEntity);
        return mav;
    }

    @PostMapping("/process_edit")
    public String processEditUser(UserEntity userEntity, Model model) {
        userService.save(userEntity);
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "console/list_users";
    }

    //usuwanie użytkownika
    @RequestMapping("/delete/{id}")
    public String deleteUserEntity(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userRepository.getById(id);
        try {
            userRepository.delete(userEntity);
        } catch (Exception e) {
            throw new ApiRequestException("Wystąpił błąd podczas usuwania użytkownika\nPrawdopodobnie użytkownik ma wypożyczone jakieś książki");
        }
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "console/list_users";
    }

    //księgozbiór wyświetlany tylko dla admina z możliwością edycji
    @GetMapping("/list_books_admin")
    public String viewEditBooksList(Model model) {
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "console/list_books_admin";
    }

    //Edycja książki
    @RequestMapping("/editbook/{id}")
    public ModelAndView showEditBookForm(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("console/edit_book");
        Book book = bookRepository.getById(id);
        mav.addObject("book", book);
        return mav;
    }

    @PostMapping("/process_edit_book")
    public String processEditBook(Book book, Model model) {
        bookService.save(book);
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "console/list_books_admin";
    }

    //usuwanie książki
    @RequestMapping("/deletebook/{id}")
    public String deleteBook(@PathVariable(name = "id") long id, Model model) {
        Book book = bookRepository.getById(id);
        try {
            bookRepository.delete(book);
        } catch (Exception e) {
            throw new ApiRequestException("Wystąpił błąd podczas usuwania książki\nPrawdopodobnie książka jest wypożyczona przez użytkownika");
        }
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "console/list_books_admin";
    }


    @GetMapping("/list_reservations")
    public String viewReservationBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.ZAREZERWOWANA);
        model.addAttribute("listReservations", listReservations);
        return "console/list_reservations";
    }

    @RequestMapping("/book_ready/{id}")
    public String bookReady(@PathVariable(name = "id") long id) {
        reservationService.updateStatus(id, ReservationStatus.GOTOWA_DO_ODBIORU);
        return "console/process_book_ready";
    }

    @GetMapping("/list_book_ready")
    public String viewReadyBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.GOTOWA_DO_ODBIORU);
        model.addAttribute("listReservations", listReservations);
        return "console/list_book_ready";
    }

    @RequestMapping("/book_taken/{id}")
    public String bookTaken(@PathVariable(name = "id") long id) {
        reservationService.updateStatus(id, ReservationStatus.WYPOZYCZONA);
        return "console/process_book_taken";
    }

    @GetMapping("/list_book_taken")
    public String viewTakenBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.WYPOZYCZONA);
        model.addAttribute("listReservations", listReservations);
        return "console/list_book_taken";
    }

    @GetMapping("/book_returned/{id}/{email}")
    public String bookReturned(@PathVariable long id, @PathVariable String email) {
        UserEntity userEntity = userRepository.getUserByEmail(email);
        reservationService.updateStatus(id, ReservationStatus.ZWROCONA);
        userEntity.setReservationsCount(userEntity.getReservationsCount() - 1);
        userRepository.save(userEntity);
        return "console/process_book_return";
    }
}
