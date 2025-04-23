package com.esoft.citytaxi.dto.reservation;

import com.esoft.citytaxi.constant.ReservationStatus;
import com.esoft.citytaxi.entity.Reservations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDto {

    private String reservationId;

    private String pickupAddress;

    private String pickupLongitude;

    private String pickupLatitude;

    private String destinationAddress;

    private String destinationLongitude;

    private String destinationLatitude;

    private Double charge;

    private String paymentStatus;

    private String paymentType;

    private ReservationStatus status;

    private String passengerUsername;

    private String passengerName;

    private String driverUsername;

    private String driverName;

    private String operatorUsername;

    private String operatorName;

    private LocalDateTime startedTime = LocalDateTime.now();

    private LocalDateTime completedTime;

    public ReservationResponseDto(Reservations reservations) {
        this.reservationId = reservations.getReservationId();
        this.pickupAddress = reservations.getPickupAddress();
        this.pickupLongitude = reservations.getPickupLongitiude();
        this.pickupLatitude = reservations.getPickupLatitude();
        this.destinationAddress = reservations.getDestinationAddress();
        this.destinationLongitude = reservations.getDestinationLongitiude();
        this.destinationLatitude = reservations.getDestinationLatitude();
        this.charge = reservations.getCharge();
        this.paymentStatus = reservations.getPaymentStatus();
        this.paymentType = reservations.getPaymentType();
        this.status = reservations.getStatus();

        // Retrieve passenger, driver, and operator usernames if entities are not null
        this.passengerUsername = reservations.getPassenger() != null ? reservations.getPassenger().getUser().getUsername() : null;
        this.passengerName = reservations.getPassenger() != null ? reservations.getPassenger().getName() : null;
        this.driverUsername = reservations.getDriver() != null ? reservations.getDriver().getUser().getUsername() : null;
        this.driverName = reservations.getDriver() != null ? reservations.getDriver().getName() : null;
        this.operatorUsername = reservations.getOperator() != null ? reservations.getOperator().getUser().getUsername() : null;
        this.operatorName = reservations.getOperator() != null ? reservations.getOperator().getName() : null;

        this.startedTime = reservations.getStartedTime();
        this.completedTime = reservations.getCompletedTime();
    }

}
