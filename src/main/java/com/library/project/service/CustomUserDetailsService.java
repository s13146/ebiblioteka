package com.library.project.service;

import com.library.project.model.CustomUserDetails;
import com.library.project.model.User;
import com.library.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Użytkownik nie znaleziony");
        }
        return new CustomUserDetails(user);
    }
}
