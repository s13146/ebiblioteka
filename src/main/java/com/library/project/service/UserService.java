package com.library.project.service;

import com.library.project.exception.ApiRequestException;
import com.library.project.model.Book;
import com.library.project.model.Group;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.BookStatus;
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

    public void setPassword(UserEntity userEntity, String password) {
        userEntity.setPassword(passwordEncoder(password));
    }
    public boolean comparePassword(UserEntity userEntity){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserEntity currentUser = getCurrentUser();
        if (encoder.matches(userEntity.getPassword(),currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder(userEntity.getNewPassword()));
            userRepository.save(currentUser);
            return true;
        } else {
            return false;
        }
    }

    private String passwordEncoder(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }


    public void updateCustomerGroup(UserEntity userEntity) {
        Group group = groupRepository.findByCode("customer");
        userEntity.addUserGroups(group);
    }

    public void addReservation(UserEntity userEntity, Book book) {
        try{

            book.setBookStatus(BookStatus.NIEDOSTEPNA);
            Reservation reservation = reservationService.create(userEntity, book);
            userEntity.setReservationsCount(userEntity.getReservationsCount() + 1);
            userEntity.addReservation(reservation);
            reservationService.save(reservation);
            userRepository.save(userEntity);
        }
        catch (Exception e){
            throw new ApiRequestException("Wystąpił błąd przy próbie rezerwacji książki");
        }
    }

    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getUserByEmail(((User) principal).getUsername());
    }
}
