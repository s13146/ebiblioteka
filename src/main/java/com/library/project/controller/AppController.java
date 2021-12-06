package com.library.project.controller;

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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppController {

    private UserService userService;
    private UserRepository userRepository;
    private BookService bookService;
    private BookRepository bookRepository;
    private ReservationService reservationService;
    private ReservationRepository reservationRepository;

    public AppController(UserService userService, UserRepository userRepository, BookService bookService, BookRepository bookRepository, ReservationService reservationService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("")
    public String viewHomePageWithBooksList(Model model) {
        List<Book> listBooks = bookRepository.findAll();
        model.addAttribute("listBooks", listBooks);
        return "index";
    }

    @GetMapping("/registration")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "registration";
    }

    @PostMapping("/process_register")
    public String processRegistration(UserEntity userEntity) {
        userService.save(userEntity);
        return "process_register";
    }

    @GetMapping("/add_book")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        return "add_book";
    }

    @PostMapping("/process_add_book")
    public String processAddBook(Book book) {
        bookService.save(book);
        return "process_add_book";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) {
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "list_users";
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
        return "list_user_books";
    }

    //Edycja użytkownika
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditUserEntityForm(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("edit_user");

        UserEntity userEntity = userRepository.getById(id);
        mav.addObject("userEntity", userEntity);
        return mav;
    }

    @PostMapping("/process_edit")
    public String processEditUser(UserEntity userEntity, Model model) {
        userService.save(userEntity);
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "list_users";
    }

    //usuwanie użytkownika
    @RequestMapping("/delete/{id}")
    public String deleteUserEntity(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userRepository.getById(id);
        userRepository.delete(userEntity);
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "list_users";
    }

    //księgozbiór wyświetlany tylko dla admina z możliwością edycji
    @GetMapping("/list_books_admin")
    public String viewEditBooksList(Model model) {
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "list_books_admin";
    }

    //Edycja książki
    @RequestMapping("/editbook/{id}")
    public ModelAndView showEditBookForm(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("edit_book");

        Book book = bookRepository.getById(id);
        mav.addObject("book", book);
        return mav;
    }

    @PostMapping("/process_edit_book")
    public String processEditBook(Book book, Model model) {
        bookService.save(book);
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "list_books_admin";
    }

    //usuwanie książki
    @RequestMapping("/deletebook/{id}")
    public String deleteBook(@PathVariable(name = "id") long id, Model model) {
        Book book = bookRepository.getById(id);
        bookRepository.delete(book);
        List<Book> listBooksAdmin = bookRepository.findAll();
        model.addAttribute("listBooksAdmin", listBooksAdmin);
        return "list_books_admin";
    }


    @RequestMapping("/book_reservation/{id}")
    public String bookReservation(@PathVariable(name = "id") long id, Model model) {
        UserEntity userEntity = userService.getCurrentUser();
        Book book = bookRepository.getById(id);
        if (userEntity == null) {
            return "error";
        } else if (userEntity.nrOfReservations() > 2) {
            System.out.println(userEntity.nrOfReservations());
            return "error";
        } else if (book.getBookStatus() == BookStatus.NOTAVAILABLE) {
            return "error";
        } else {
            book.setBookStatus(BookStatus.NOTAVAILABLE);
            model.addAttribute("book", book);
            userService.addReservation(userEntity, book);
            return "process_book_reservation";
        }
    }

    @GetMapping("/list_reservations")
    public String viewReservationBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.BOOKED);
        model.addAttribute("listReservations", listReservations);
        return "list_reservations";
    }

    @RequestMapping("/book_ready/{id}")
    public String bookReady(@PathVariable(name = "id") long id) {
        reservationService.updateStatus(id, ReservationStatus.READYFORPICKUP);
        return "process_book_ready";
    }

    @GetMapping("/list_book_ready")
    public String viewReadyBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.READYFORPICKUP);
        model.addAttribute("listReservations", listReservations);
        return "list_book_ready";
    }

    @RequestMapping("/book_taken/{id}")
    public String bookTaken(@PathVariable(name = "id") long id) {
        reservationService.updateStatus(id, ReservationStatus.TAKEN);
        return "process_book_taken";
    }

    @GetMapping("/list_book_taken")
    public String viewTakenBooksList(Model model) {
        List<Reservation> listReservations = reservationRepository.findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus.TAKEN);
        model.addAttribute("listReservations", listReservations);
        return "list_book_taken";
    }

    @RequestMapping("/book_returned/{id}")
    public String bookReturned(@PathVariable(name = "id") long id) {
        reservationService.updateStatus(id, ReservationStatus.RETURNED);

        return "process_book_return";
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
        return "customer_details";
    }

    @GetMapping("/error")
    public String anyError() {
        return "error";
    }
}
