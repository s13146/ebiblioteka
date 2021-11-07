package com.library.project.service;

import com.library.project.model.CustomUserDetails;
import com.library.project.model.Group;
import com.library.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.library.project.model.User user = userRepository.getUserByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Użytkownik nie znaleziony");
        }
        UserDetails userDetails = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .disabled(user.isEnabled())
                .authorities(getAuthorities(user)).build()
                ;



        return userDetails;
    }

    private Collection<GrantedAuthority> getAuthorities(com.library.project.model.User user){
        Set<Group> userGroups = user.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
        for(Group userGroup : userGroups){
            authorities.add(new SimpleGrantedAuthority(userGroup.getCode().toUpperCase()));
        }

        return authorities;
    }
}
