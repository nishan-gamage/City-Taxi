
package com.esoft.citytaxi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "VehicleTypes")
public class VehicleType extends BaseEntity {

    private String name;

    private String code;

    private Double perKmCharge;

}
