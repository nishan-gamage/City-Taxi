
package com.esoft.citytaxi.entity;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.entity.transactioanal.DriverLocation;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "Driver")
public class Driver extends BaseEntity {

    private String name;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @Column(name = "current_longitiude")
    private String currentLongitude;

    @Column(name = "current_latitude")
    private String currentLatitude;

    @Enumerated(EnumType.STRING)
    private DriverStatuses status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private DriverLocation location;

}
