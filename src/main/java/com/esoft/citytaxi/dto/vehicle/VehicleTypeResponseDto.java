package com.esoft.citytaxi.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleTypeResponseDto {
    private Long id;
    private String name;
    private String code;
    private Double perKmCharge;
}
