package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.entity.VehicleType;
import com.esoft.citytaxi.repository.VehicleTypeRepository;
import com.esoft.citytaxi.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public Optional<VehicleType> findVehicleTypeById(Long id) {
        return this.vehicleTypeRepository.findById(id);
    }

    @Override
    public List<VehicleType> findAll() {
        return this.vehicleTypeRepository.findAll();
    }
}
