package com.library.project.service;

import com.library.project.model.Book;
import com.library.project.model.Group;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.repository.UserGroupRepository;
import com.library.project.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserGroupRepository groupRepository;
    private ReservationService reservationService;

    public UserService(UserRepository userRepository, UserGroupRepository groupRepository, ReservationService reservationService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.reservationService = reservationService;
    }

    public UserEntity save(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder(userEntity.getPassword()));
        updateCustomerGroup(userEntity);
        return userRepository.save(userEntity);
    }

    public String passwordEncoder(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
    }


    private void updateCustomerGroup(UserEntity userEntity) {
        Group group = groupRepository.findByCode("customer");
        userEntity.addUserGroups(group);
    }

    public void addReservation(UserEntity userEntity, Book book){
        Reservation reservation = reservationService.create(userEntity,book);

        userEntity.addReservation(reservation);
        reservationService.save(reservation);
    }

    public UserEntity getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.getUserByEmail(((User)principal).getUsername());
        return userEntity;
    }
}
