package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.transactioanal.DriverLocation;

import java.util.List;
import java.util.Optional;

public interface DriverLocationService {

    void save(DriverLocation driverLocation);

    List<DriverLocation> findNearbyDrivers(double pickupLat, double pickupLon, double radius);
    Optional<DriverLocation> findByUsername(String driverUsername);
}
