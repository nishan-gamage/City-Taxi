package com.esoft.citytaxi.dto.passenger;

import com.esoft.citytaxi.constant.UserStatuses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequestDto {

    private String name;

    private String contactNumber;

    private UserStatuses status;

}
