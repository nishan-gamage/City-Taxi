package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.constant.ReservationStatus;
import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.dto.reservation.ReservationRequestDto;
import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Operator;
import com.esoft.citytaxi.entity.Passenger;
import com.esoft.citytaxi.entity.Reservations;
import com.esoft.citytaxi.exception.ResourceNotFoundException;
import com.esoft.citytaxi.service.DriverService;
import com.esoft.citytaxi.service.OperatorService;
import com.esoft.citytaxi.service.PassengerService;
import com.esoft.citytaxi.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationsController {

    private final PassengerService passengerService;

    private final DriverService driverService;

    private final OperatorService operatorService;

    private final ReservationService reservationService;

    public ReservationsController(PassengerService passengerService, DriverService driverService, OperatorService operatorService, ReservationService reservationService) {
        this.passengerService = passengerService;
        this.driverService = driverService;
        this.operatorService = operatorService;
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation-id")
    public ResponseEntity<AppResponse<ReservationResponseDto>> getByReservationId(
            @RequestParam("reservationId") String reservationId
    ) {

        AppResponse<ReservationResponseDto> response = new AppResponse<>();

        try {

            Reservations reservations = reservationService.findByReservationId(reservationId);

            response.setMessage("Success");
            response.setStatusCode(200);
            response.setLocalDateTime(LocalDateTime.now());
            response.setData(new ReservationResponseDto(reservations));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setLocalDateTime(LocalDateTime.now());
            response.setMessage(Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<ReservationResponseDto>>> getAll() {

        AppResponse<List<ReservationResponseDto>> response = new AppResponse<>();

        try {

            List<Reservations> reservations = reservationService.findAll();

            response.setMessage("Success");
            response.setStatusCode(200);
            response.setLocalDateTime(LocalDateTime.now());
            response.setData(reservations.stream().map(ReservationResponseDto::new).collect(Collectors.toList()));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setLocalDateTime(LocalDateTime.now());
            response.setMessage(Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<AppResponse<ReservationResponseDto>> add(
            @RequestBody ReservationRequestDto request
    ) {

        AppResponse<ReservationResponseDto> response = new AppResponse<>();

        try {

            Passenger passenger = passengerService.findByUsername(request.getPassengerUsername());

            Driver driver = driverService.getDriverDetailsByUsername(request.getDriverUsername());

            Operator operator = null;

            if(request.getOperatorUsername() != null) {
                operator = operatorService.findByUserName(request.getOperatorUsername());
            }

            Reservations reservations = Reservations
                    .builder()
                    .charge(request.getCharge())
                    .operator(operator)
                    .driver(driver)
                    .passenger(passenger)
                    .destinationAddress(request.getDestinationAddress())
                    .destinationLatitude(request.getDestinationLatitude())
                    .destinationLongitiude(request.getDestinationLongitude())
                    .pickupAddress(request.getPickupAddress())
                    .pickupLatitude(request.getPickupLatitude())
                    .pickupLongitiude(request.getPickupLongitude())
                    .paymentStatus(request.getPaymentStatus())
                    .paymentType(request.getPaymentType())
                    .status(ReservationStatus.DRIVER_ACCEPTED)
                    .startedTime(LocalDateTime.now())
                    .reservationId(UUID.randomUUID().toString())
                    .build();

            Reservations added = reservationService.add(reservations);

            response.setMessage("Success");
            response.setStatusCode(201);
            response.setLocalDateTime(LocalDateTime.now());
            response.setData(new ReservationResponseDto(added));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setLocalDateTime(LocalDateTime.now());
            response.setMessage(Arrays.toString(e.getStackTrace()));
        } catch (ResourceNotFoundException e) {
            response.setStatusCode(404);
            response.setLocalDateTime(LocalDateTime.now());
            response.setMessage(Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/reservation-id/{reservationId}")
    public ResponseEntity<AppResponse<ReservationResponseDto>> update(
            @PathVariable("reservationId") String reservationId,
            @RequestParam("reservationStatus") ReservationStatus status
    ) {

        AppResponse<ReservationResponseDto> response = new AppResponse<>();

        try {

            Reservations reservations = reservationService.findByReservationId(reservationId);
            reservations.setStatus(status);
            if(ReservationStatus.COMPLETE.equals(status)) {
                reservations.setPaymentStatus("PAID");
            }
            Reservations added = reservationService.update(reservations);

            response.setMessage("Success");
            response.setStatusCode(200);
            response.setLocalDateTime(LocalDateTime.now());
            response.setData(new ReservationResponseDto(added));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setLocalDateTime(LocalDateTime.now());
            response.setMessage(Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
