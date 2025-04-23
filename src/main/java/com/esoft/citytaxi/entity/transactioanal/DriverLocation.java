package com.esoft.citytaxi.entity.transactioanal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "driver_locations", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class DriverLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private double lat;

    private double lon;

    @Transient
    private double distance;

}