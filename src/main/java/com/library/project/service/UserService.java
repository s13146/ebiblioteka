package com.library.project.service;

import com.library.project.model.User;
import com.library.project.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){
        user.setPassword(passwordEncoder(user.getPassword()));
        return userRepository.save(user);
    }

    public String passwordEncoder(String rawPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
    }
}
