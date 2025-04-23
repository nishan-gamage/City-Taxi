package com.esoft.citytaxi.dto.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateRequestDto {

    private int value;

    private String review;

    private String reservationId;

}
