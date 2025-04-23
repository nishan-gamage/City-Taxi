package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.Passenger;
import com.esoft.citytaxi.entity.User;

import java.util.List;

public interface PassengerService {

    Passenger save(Passenger passenger);

    Passenger savePassenger(Passenger passenger);

    Passenger findByUser(User user);

    List<Passenger> findAll();

    Passenger findByUsername(String username);
}
