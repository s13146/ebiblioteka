package com.library.project.service;

import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.BookStatus;
import com.library.project.model.enums.ReservationStatus;
import com.library.project.repository.ReservationRepository;
import com.library.project.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Reservation create(UserEntity userEntity, Book book) {
        return new Reservation(userEntity, book, ReservationStatus.BOOKED);
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void updateStatus(long id, ReservationStatus reservationStatus) {
        Reservation reservation = reservationRepository.getById(id);
        reservation.setReservationStatus(reservationStatus);
        if (reservationStatus == ReservationStatus.RETURNED) {
            UserEntity userEntity = reservation.getUserEntity();
            userEntity.deleteReservation(reservation);
            userRepository.save(userEntity);
            reservation.getBook().setBookStatus(BookStatus.AVAILABLE);

        }
        reservationRepository.save(reservation);
    }
}
