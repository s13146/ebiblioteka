package com.library.project.repository;

import com.library.project.model.Reservation;
import com.library.project.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationStatusOrderByBorrowDateDesc(ReservationStatus reservationStatus);
}
