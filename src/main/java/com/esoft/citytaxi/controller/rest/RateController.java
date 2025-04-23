package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.dto.rate.RateRequestDto;
import com.esoft.citytaxi.dto.rate.RateResponseDto;
import com.esoft.citytaxi.entity.Rates;
import com.esoft.citytaxi.entity.Reservations;
import com.esoft.citytaxi.service.RateService;
import com.esoft.citytaxi.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rates")
@CrossOrigin
public class RateController {

    private final RateService rateService;

    private final ReservationService reservationService;

    public RateController(RateService rateService, ReservationService reservationService) {
        this.rateService = rateService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<AppResponse<String>> add(
            @RequestBody RateRequestDto rateRequestDto
    ) {

        AppResponse<String> response = new AppResponse<>();

        try {

            Reservations reservations = reservationService.findByReservationId(rateRequestDto.getReservationId());

            rateService.add(Rates.builder()
                    .value(rateRequestDto.getValue())
                    .review(rateRequestDto.getReview())
                    .driver(reservations.getDriver())
                    .passenger(reservations.getPassenger())
                    .build());

            response.setStatusCode(201);
            response.setMessage("Success");

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Failed: " + e.getMessage());
        }

        response.setLocalDateTime(LocalDateTime.now());
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping
    public ResponseEntity<AppResponse<List<RateResponseDto>>> getByDriver(
            @RequestParam("driverUsername") String driverUsername
    ) {

        AppResponse<List<RateResponseDto>> response = new AppResponse<>();

        try {

            List<Rates> rates = rateService.findByDriverUsername(driverUsername);

            response.setData(rates.stream().map(RateResponseDto::new).collect(Collectors.toList()));
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
