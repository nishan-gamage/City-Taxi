package com.esoft.citytaxi.dto.rate;

import com.esoft.citytaxi.entity.Rates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponseDto {

    private int value;

    private String review;

    private String passengerName;

    private LocalDateTime dateTime;

    public RateResponseDto(Rates rates) {
        this.value = rates.getValue();
        this.review = rates.getReview();
        this.passengerName = rates.getPassenger().getName();
        this.dateTime = rates.getCreatedAt();
    }

}
