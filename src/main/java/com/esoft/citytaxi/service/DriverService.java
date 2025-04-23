package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.exception.ResourceNotFoundException;

import java.util.List;

public interface DriverService {

    Driver save(Driver driver);

    Driver update(Driver driver);

    Driver findByUser(User user);

    List<Driver> findNearbyDrivers(double pickupLat, double pickupLon, double radius);

    Driver getDriverDetailsByUsername(String username) throws ResourceNotFoundException;

    List<Driver> findAll();

    Driver findByUsername(String username);
}
