package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.entity.VehicleType;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Registered
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    Optional<VehicleType> findByCode(String code);
}
