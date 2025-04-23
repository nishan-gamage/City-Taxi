package com.esoft.citytaxi.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequestDto {

    private String pickupAddress;

    private String pickupLongitude;

    private String pickupLatitude;

    private String destinationAddress;

    private String destinationLongitude;

    private String destinationLatitude;

    private Double charge;

    private String paymentStatus;

    private String paymentType;

    private String status;

    private String passengerUsername;

    private String driverUsername;

    private String operatorUsername;

    private LocalDateTime startedTime = LocalDateTime.now();

    private LocalDateTime completedTime;

}
