package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.dto.passenger.PassengerRequestDto;
import com.esoft.citytaxi.dto.passenger.PassengerResponseDto;
import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.entity.Passenger;
import com.esoft.citytaxi.entity.Reservations;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.UserRole;
import com.esoft.citytaxi.service.PassengerService;
import com.esoft.citytaxi.service.ReservationService;
import com.esoft.citytaxi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    private final ReservationService reservationService;

    private final UserService userService;

    public PassengerController(PassengerService passengerService, ReservationService reservationService, UserService userService) {
        this.passengerService = passengerService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AppResponse<PassengerResponseDto>> save(
            @RequestBody PassengerRequestDto request
    ) {
        AppResponse<PassengerResponseDto> response = new AppResponse<>();

        try {

            Passenger dbData = passengerService.findByUsername(cleanString(request.getContactNumber()));

            if(dbData == null) {
                Passenger passenger = Passenger.builder()
                        .name(request.getName())
                        .user(User.builder()
                                .contactNumber(request.getContactNumber())
                                .status(UserStatuses.ACTIVE)
                                .username(cleanString(request.getContactNumber()))
                                .password(cleanString(request.getContactNumber()))
                                .role(UserRole.builder().code(UserRoles.PASSENGER).build())
                                .build())
                        .build();

                passengerService.save(passenger);
                response.setStatusCode(201);
                response.setMessage("Success");

            } else {
                response.setStatusCode(400);
                response.setMessage("Already exist");
            }

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<PassengerResponseDto>>> getAllPassengers() {
        List<PassengerResponseDto> reponseDtos = this.passengerService.findAll()
                .stream().map(PassengerResponseDto::new).toList();
        AppResponse<List<PassengerResponseDto>> body = new AppResponse<>();
        body.setMessage("Success");
        body.setData(reponseDtos);
        body.setStatusCode(HttpStatus.OK.value());
        body.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppResponse<PassengerResponseDto>> getDriverDetailsByUsername(@PathVariable String username) {
        AppResponse<PassengerResponseDto> response = new AppResponse<>();

        try {

            Passenger passenger = passengerService.findByUsername(username);
            PassengerResponseDto responseDto = new PassengerResponseDto(passenger);

            Optional<Reservations> reservations = reservationService.findActiveReservationForPassenger(passenger.getUser().getUsername());
            reservations.ifPresent(value -> responseDto.setActiveReservation(new ReservationResponseDto(value)));

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setData(responseDto);

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<AppResponse<PassengerResponseDto>> Update(
            @PathVariable String username,
            @RequestBody PassengerRequestDto request
    ) {
        AppResponse<PassengerResponseDto> response = new AppResponse<>();

        try {

            Passenger passenger = passengerService.findByUsername(username);

            if(!passenger.getUser().getContactNumber().equalsIgnoreCase(request.getContactNumber())) {
                Passenger dbData = passengerService.findByUsername(cleanString(request.getContactNumber()));
                if(dbData != null) {
                    response.setStatusCode(400);
                    response.setMessage("Contact number already exist");
                    response.setLocalDateTime(LocalDateTime.now());
                    return ResponseEntity.status(response.getStatusCode()).body(response);
                }
            }

            passenger.setName(request.getName());
            passenger.getUser().setContactNumber(request.getContactNumber());
            passenger.getUser().setStatus(request.getStatus());
            passengerService.savePassenger(passenger);

            response.setStatusCode(200);
            response.setMessage("Success");

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }
        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    public static String cleanString(String input) {
        return input.toUpperCase().replaceAll("[^0-9A-Z]", "_");
    }

}
