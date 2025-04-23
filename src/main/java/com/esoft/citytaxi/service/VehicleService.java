package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.VehicleType;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Optional<VehicleType> findVehicleTypeById(Long id);

    List<VehicleType> findAll();
}
