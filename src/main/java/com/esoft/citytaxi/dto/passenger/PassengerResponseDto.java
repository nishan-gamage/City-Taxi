package com.esoft.citytaxi.dto.passenger;


import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.dto.user.UserResponseDto;
import com.esoft.citytaxi.entity.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponseDto {
    private Long id;
    private String name;
    private UserResponseDto user;
    private ReservationResponseDto activeReservation;

    public PassengerResponseDto(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getName();
        if (Objects.nonNull(passenger.getUser())) {
            this.user = new UserResponseDto(passenger.getUser());
        }
    }
}
