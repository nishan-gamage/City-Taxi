package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.Reservations;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservations add(Reservations reservations);

    Reservations findByReservationId(String reservationId);

    Optional<Reservations> findActiveReservationForPassenger(String passengerUserName);

    Optional<Reservations> findActiveReservationForDriver(String driverUserName);

    Reservations update(Reservations reservations);

    List<Reservations> findAll();

}
