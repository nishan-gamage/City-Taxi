package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.dto.driver.DriverRequestDto;
import com.esoft.citytaxi.dto.driver.DriverResponseDto;
import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.dto.user.UserResponseDto;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Rates;
import com.esoft.citytaxi.entity.Reservations;
import com.esoft.citytaxi.exception.ResourceNotFoundException;
import com.esoft.citytaxi.service.DriverService;
import com.esoft.citytaxi.service.RateService;
import com.esoft.citytaxi.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    private final ReservationService reservationService;

    private final RateService rateService;

    public DriverController(DriverService driverService, ReservationService reservationService, RateService rateService) {
        this.driverService = driverService;
        this.reservationService = reservationService;
        this.rateService = rateService;
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<DriverResponseDto>>> getAllPassengers() {
        List<DriverResponseDto> reponseDtos = this.driverService.findAll()
                .stream().map(DriverResponseDto::new).toList();
        AppResponse<List<DriverResponseDto>> body = new AppResponse<>();
        body.setMessage("Success");
        body.setData(reponseDtos);
        body.setStatusCode(HttpStatus.OK.value());
        body.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/nearby")
    public ResponseEntity<AppResponse<List<DriverResponseDto>>> getNearbyDrivers(
            @RequestParam double pickupLat, @RequestParam double pickupLon, @RequestParam double radius
    ) {
        AppResponse<List<DriverResponseDto>> response = new AppResponse<>();
        try {
            List<Driver> nearbyDrivers = driverService.findNearbyDrivers(pickupLat, pickupLon, radius);

            List<DriverResponseDto> driversList = nearbyDrivers.stream()
                    .map(driver -> DriverResponseDto.builder()
                            .id(driver.getId())
                            .name(driver.getName())
                            .vehicleNumber(driver.getVehicleNumber())
                            .vehicleType(driver.getVehicleType())
                            .status(driver.getStatus())
                            .user(new UserResponseDto(driver.getUser().getUsername(), driver.getUser().getContactNumber()))
                            .currentLat(driver.getLocation().getLat())
                            .currentLog(driver.getLocation().getLon())
                            .distanceToPickupPoint(driver.getLocation().getDistance())
                            .vehicleModel(driver.getVehicleModel())
                            .build())
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setData(driversList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }

        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppResponse<DriverResponseDto>> getDriverDetailsByUsername(@PathVariable String username) {
        AppResponse<DriverResponseDto> response = new AppResponse<>();

        try {

            Driver driver = driverService.getDriverDetailsByUsername(username);

            List<Rates> rates = rateService.findByDriver(driver);

            Optional<Reservations> reservation = reservationService.findActiveReservationForDriver(driver.getUser().getUsername());

            DriverResponseDto driverResponseDto= new DriverResponseDto(driver);

            reservation.ifPresent(value -> driverResponseDto.setActiveReservation(new ReservationResponseDto(value)));

            if(!rates.isEmpty()) {
                driverResponseDto.setRateAverage(rates.stream()
                        .mapToInt(Rates::getValue)
                        .average()
                        .orElse(0.0));
                driverResponseDto.setTotalRates(rates.size());
            }

            if(driver.getLocation() != null) {
                driverResponseDto.setCurrentLat(driver.getLocation().getLat());
                driverResponseDto.setCurrentLog(driver.getLocation().getLon());
                driverResponseDto.setDistanceToPickupPoint(driver.getLocation().getDistance());
            }

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setData(driverResponseDto);

        } catch (ResourceNotFoundException e) {
            response.setStatusCode(404);
            response.setMessage("Failed: " + e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<AppResponse<DriverResponseDto>> update(
            @PathVariable String username,
            @RequestBody DriverRequestDto request
    ) {
        AppResponse<DriverResponseDto> response = new AppResponse<>();

        try {
            Driver driver = driverService.findByUsername(username);

            driver.setName(request.getName());
            driver.getUser().setContactNumber(request.getContactNumber());
            driver.getUser().setStatus(request.getStatus());
            driverService.update(driver);

            response.setStatusCode(200);
            response.setMessage("Success");

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/driver-status/{username}")
    public ResponseEntity<AppResponse<DriverResponseDto>> update(
            @PathVariable String username,
            @RequestParam DriverStatuses status
    ) {
        AppResponse<DriverResponseDto> response = new AppResponse<>();

        try {
            Driver driver = driverService.findByUsername(username);

            driver.setStatus(status);
            driverService.update(driver);

            response.setStatusCode(200);
            response.setMessage("Success");

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}

