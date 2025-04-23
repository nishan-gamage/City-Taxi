package com.esoft.citytaxi.dto.driver;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.dto.user.UserResponseDto;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverResponseDto {

    private Long id;

    private String name;

    private String vehicleNumber;

    private VehicleType vehicleType;

    private String vehicleModel;

    private DriverStatuses status;

    private UserResponseDto user;

    private Double currentLog;

    private Double currentLat;

    private Double distanceToPickupPoint;

    private int totalRates;

    private double rateAverage;

    private ReservationResponseDto activeReservation;

    public DriverResponseDto(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getName();
        this.vehicleNumber = driver.getVehicleNumber();
        this.vehicleType = driver.getVehicleType();
        this.status = driver.getStatus();
        this.user = new UserResponseDto(driver.getUser());
    }
}
