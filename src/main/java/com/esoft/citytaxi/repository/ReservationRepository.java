package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.constant.ReservationStatus;
import com.esoft.citytaxi.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    Reservations findReservationsByReservationId(String reservationId);

    List<Reservations> findReservationsByPassenger_User_UsernameAndStatusInOrderByIdDesc(String passengerUsername, List<ReservationStatus> statuses);

    List<Reservations> findAllByOrderByIdDesc();

    List<Reservations> findReservationsByDriver_User_UsernameAndStatusInOrderByIdDesc(String driverUsername, List<ReservationStatus> statuses);
}
