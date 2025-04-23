
package com.esoft.citytaxi.entity;

import com.esoft.citytaxi.constant.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "Reservations")
public class Reservations extends BaseEntity {

    @Column(name = "reservation_id")
    private String reservationId;

    @Column(name = "pickup_longitiude")
    private String pickupLongitiude;

    @Column(name = "pickup_latitude")
    private String pickupLatitude;

    private String pickupAddress;

    @Column(name = "destination_longitiude")
    private String destinationLongitiude;

    @Column(name = "destination_latitude")
    private String destinationLatitude;

    private String destinationAddress;

    private Double charge;

    private String paymentStatus;

    private String paymentType;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Operator operator;

    @Column(name = "started_time")
    private LocalDateTime startedTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

}
