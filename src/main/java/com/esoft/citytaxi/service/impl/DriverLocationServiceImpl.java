package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.entity.transactioanal.DriverLocation;
import com.esoft.citytaxi.repository.DriverLocationRepository;
import com.esoft.citytaxi.service.DriverLocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverLocationServiceImpl implements DriverLocationService {

    private final DriverLocationRepository repository;

    public DriverLocationServiceImpl(DriverLocationRepository driverLocationRepository) {
        this.repository = driverLocationRepository;
    }

    @Override
    public void save(DriverLocation driverLocation) {
         repository.upsertDriverLocation(
                 driverLocation.getUsername(),
                 driverLocation.getLat(),
                 driverLocation.getLon()
         );
    }

    @Override
    public List<DriverLocation> findNearbyDrivers(double pickupLat, double pickupLon, double radius) {
        return repository.findNearbyDrivers(pickupLat, pickupLon, radius);
    }

    @Override
    public Optional<DriverLocation> findByUsername(String driverUsername) {
        return repository.findByUsername(driverUsername);
    }

}
