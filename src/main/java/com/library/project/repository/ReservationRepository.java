package com.library.project.repository;

import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import com.library.project.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus reservationStatus);

    @Query("SELECT r FROM Reservation r WHERE r.userEntity.email LIKE %?1%")
    List<Reservation> getMyReservation(String keyword);

}
