package com.esoft.citytaxi.dto.auth.request;

import com.esoft.citytaxi.entity.Passenger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PassengerRegistrationRequestDto extends UserRegistrationRequestDto {
    private String name;

    @JsonIgnore
    public Passenger getPassenger() {
        return Passenger.builder()
                .name(this.name)
                .user(super.getUser())
                .build();
    }
}
