package com.esoft.citytaxi.dto.driver;

import com.esoft.citytaxi.constant.UserStatuses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRequestDto {

    private String name;

    private String contactNumber;

    private UserStatuses status;

}
