package com.esoft.citytaxi.dto.auth.request;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverRegistrationRequestDto extends UserRegistrationRequestDto {
    private String name;
    private String vehicleNumber;
    private String vehicleModel;
    private Long vehicleTypeId;

    @JsonIgnore
    public Driver getDriver(VehicleType vehicleType) {
        return Driver.builder()
                .name(this.name)
                .vehicleType(vehicleType)
                .vehicleNumber(this.vehicleNumber)
                .vehicleModel(this.vehicleModel)
                .user(super.getUser(UserRoles.DRIVER))
                .status(DriverStatuses.AVAILABLE)
                .build();
    }
}
