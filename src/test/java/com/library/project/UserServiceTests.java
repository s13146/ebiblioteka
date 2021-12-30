package com.library.project;

import com.library.project.model.Book;
import com.library.project.model.Group;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.ReservationStatus;
import com.library.project.repository.ReservationRepository;
import com.library.project.repository.UserGroupRepository;
import com.library.project.repository.UserRepository;
import com.library.project.service.ReservationService;
import com.library.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserGroupRepository groupRepository;
    @Mock
    private ReservationService reservationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    void addReservation(){
        //Given
        UserEntity userEntity = new UserEntity();
        Book book = new Book();
        Reservation reservation = new Reservation(userEntity,book, ReservationStatus.ZAREZERWOWANA);
        when(reservationRepository.save(reservation)).thenReturn(new Reservation(userEntity,book, ReservationStatus.ZAREZERWOWANA));
        //When
        Reservation reservation1 = reservationRepository.save(reservation);
        //Then
        assertThat(reservation1.getId()).isEqualTo(0L);
    }
}
