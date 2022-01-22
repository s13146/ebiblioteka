package com.library.project.controller;


import com.library.project.exception.ApiRequestException;
import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.BookStatus;
import com.library.project.model.enums.ReservationStatus;
import com.library.project.repository.BookRepository;
import com.library.project.repository.ReservationRepository;
import com.library.project.repository.UserRepository;
import com.library.project.service.BookService;
import com.library.project.service.ReservationService;
import com.library.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    private com.library.project.email.MailService mailService;
    private TemplateEngine templateEngine;

    public ConsoleController(UserService userService, UserRepository userRepository, BookService bookService, BookRepository bookRepository, ReservationService reservationService, ReservationRepository reservationRepository, com.library.project.email.MailService mailService, TemplateEngine templateEngine) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/registration")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "console/registration";
    }

    @PostMapping("/process_register")
    public String processRegistration(UserEntity userEntity) {
        userService.updateCustomerGroup(userEntity);
        userService.setPassword(userEntity, userEntity.getPassword());
        userEntity.setIsEnabled(0);
        userRepository.save(userEntity);

        Context context = new Context();
        context.setVariable("header", "header");
        context.setVariable("title", "Twoje konto w eBibliotece");
        context.setVariable("userName", userEntity.getFirstName() + " " + userEntity.getLastName());
        String body = templateEngine.process("email/registration_template", context);
        mailService.sendEmail(userEntity.getEmail(), "eBiblioteka", body);

        return "console/process_register";


    }

    @GetMapping("/add_book")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        return "console/add_book";
    }

    @PostMapping("/process_add_book")
    public String processAddBook(Book book) {
        if (book == null) {
            return "error";
        }
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
    public String showEditUserEntityForm(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userRepository.getById(id);
        if (userEntity == null) {
            return "error";
        }
        model.addAttribute("userEntity", userEntity);
        return "console/edit_user";
    }

    @PostMapping("/process_edit")
    public String processEditUser(UserEntity givenUserEntity, Model model) {
        UserEntity userEntity = userRepository.getById(givenUserEntity.getId());

        userEntity.setFirstName(givenUserEntity.getFirstName());
        userEntity.setLastName(givenUserEntity.getLastName());
        userEntity.setEmail(givenUserEntity.getEmail());
        userEntity.setIsEnabled(givenUserEntity.getIsEnabled());
        
        userRepository.save(userEntity);

        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "console/list_users";
    }

    //usuwanie użytkownika
    @RequestMapping("/delete/{id}")
    public String deleteUserEntity(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userRepository.getById(id);
        if (userEntity == null) {
            return "error";
        } else if (userEntity.getReservationsCount() != 0) {
            throw new ApiRequestException("Użytkownik ma na stanie wypożyczone ksiązki");
        }
        userRepository.delete(userEntity);
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "console/list_users";
    }

    @GetMapping("/list_books_admin")
    public String viewEditBooksList(Model model) {
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "console/list_books_admin";
    }

    @RequestMapping("/editbook/{id}")
    public String showEditBookForm(@PathVariable(name = "id") long id, Model model) {
        Book book = bookRepository.getById(id);
        if (book == null) {
            return "error";
        }
        model.addAttribute("book", book);
        return "console/edit_book";
    }

    @PostMapping("/process_edit_book")
    public String processEditBook(Book book, Model model) {
        if (book == null) {
            throw new ApiRequestException("Nie udało się zedytować książki");
        }
        bookRepository.save(book);
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "console/list_books_admin";
    }

    //usuwanie książki
    @RequestMapping("/deletebook/{id}")
    public String deleteBook(@PathVariable(name = "id") long id, Model model) {
        Book book = bookRepository.getById(id);
        if (book.getBookStatus() == BookStatus.NIEDOSTEPNA)
            throw new ApiRequestException("Książka jest wypożyczona przez użytkownika i nie można jej usunąć");

        bookRepository.delete(book);
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

    @RequestMapping("/book_ready/{id}/{email}")
    public String bookReady(@PathVariable long id, @PathVariable String email) {
        UserEntity userEntity = userRepository.getUserByEmail(email);
        if (userEntity == null) {
            return "error";
        }
        if (reservationRepository.getById(id) == null) {
            return "error";
        }
        reservationService.updateStatus(id, ReservationStatus.GOTOWA_DO_ODBIORU);
        reservationService.setLendingTime(reservationRepository.getById(id));

        Context context = new Context();
        context.setVariable("header", "header");
        context.setVariable("title", "Książka gotowa do odbioru");
        context.setVariable("userName", userEntity.getFirstName() + " " + userEntity.getLastName());
        context.setVariable("book", bookRepository.getBookById(id).getTitle());
        String body = templateEngine.process("email/book_ready_template", context);

        mailService.sendEmail(userEntity.getEmail(), "eBiblioteka", body);

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
        if (userEntity == null) {
            return "error";
        }
        reservationService.updateStatus(id, ReservationStatus.ZWROCONA);
        userEntity.setReservationsCount(userEntity.getReservationsCount() - 1);
        userRepository.save(userEntity);
        return "console/process_book_return";
    }


    @GetMapping("/search_user")
    public String viewSearchUser() {
        return "console/search_user";
    }


    @PostMapping("/search_user_result")
    public String viewUserSearchResult(Model model, @RequestParam String name) {
        List<UserEntity> listUsers = userRepository.getUsersByEmail(name);
        model.addAttribute("listUsers", listUsers);
        return "console/search_user_result";
    }

    @GetMapping("/search_book_console")
    public String viewSearchBook() {
        return "console/search_book_console";
    }

    @PostMapping("/search_book_result")
    public String viewSearchResult(Model model, @RequestParam String name) {
        List<Book> bookList = bookRepository.getBookByTitleAuthorCategory(name);
        model.addAttribute("bookList", bookList);
        return "console/search_book_result";
    }
    @PostMapping("/search_book_result_console")
    public String viewSearchResultConsole(Model model, @RequestParam String name) {
        List<Book> bookList = bookRepository.getBookByTitleAuthorCategory(name);
        model.addAttribute("bookList", bookList);
        return "console/search_book_result_console";
    }
    @RequestMapping("/list_user_books/{email}")
    public String viewMyBooksList(Model model, @PathVariable String email) {
        List<Reservation> mybooks = reservationRepository.getMyReservation(email);
        model.addAttribute("mybooks", mybooks);
        model.addAttribute("email", email);

        return "console/list_user_books";
    }
}
