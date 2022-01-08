package com.library.project.controller;

import com.library.project.email.MailService;
import com.library.project.model.Book;
import com.library.project.model.UserEntity;
import com.library.project.repository.BookRepository;
import com.library.project.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class AppController {

    private UserService userService;
    private BookRepository bookRepository;
    private MailService mailService;
    private TemplateEngine templateEngine;

    public AppController(UserService userService, BookRepository bookRepository, MailService mailService, TemplateEngine templateEngine) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("")
    public String viewHomePageWithBooksList(Model model) {
        List<Book> listBooks = bookRepository.findAll();
        model.addAttribute("listBooks", listBooks);
        return "index";
    }


    @GetMapping("/console")
    public String viewConsole(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
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

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        String token = RandomString.make(30);
        userService.updateResetPasswordToken(token, email);
        String resetPasswordLink = "http://localhost:8080/reset_password?token=" + token;

        Context context = new Context();
        context.setVariable("header", "Przypomnienie hasła");
        context.setVariable("title", "eBiblioteka - przypomnienie hasła");
        context.setVariable("description", resetPasswordLink);
        String body = templateEngine.process("email/forgot_password_template", context);

        mailService.sendEmail(email, "eBiblioteka - przypomnienie hasła", body);
        Runnable task = () -> userService.deleteToken(token);
        ses.schedule(task, 2, TimeUnit.MINUTES);
        ses.shutdown();
        model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        return "forgot_password_form";
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        UserEntity userEntity = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (userEntity == null) {
            return "error";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestParam String token, @RequestParam String password) {
        UserEntity userEntity = userService.getByResetPasswordToken(token);

        if (userEntity == null) {
            return "error";
        } else {
            userService.updatePassword(userEntity, password);
        }

        return "process_forgot_password";

    }


}
