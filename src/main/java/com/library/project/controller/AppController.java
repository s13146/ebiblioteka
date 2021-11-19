package com.library.project.controller;

import com.library.project.model.UserEntity;
import com.library.project.repository.UserRepository;
import com.library.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AppController {

    private UserService userService;
    private UserRepository userRepository;

    public AppController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/rejestracja")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "rejestracja";
    }

    @PostMapping("/process_register")
    public String processRegistration(UserEntity userEntity) {
        userService.save(userEntity);
        return "rejestracja_udana";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) {
        List<UserEntity> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users_list";
    }

    @GetMapping("/console")
    public String viewConsole() {
        return "console";
    }
    @GetMapping("/login")
    public String viewLogin() {return "login";}
    @GetMapping("/customer")
    public String viewCustomerPage(){return "customer";}
}
