package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.entity.transactioanal.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO driver_locations (username, lat, lon) " +
            "VALUES (:username, :lat, :lon) " +
            "ON CONFLICT (username) " +
            "DO UPDATE SET lat = EXCLUDED.lat, lon = EXCLUDED.lon",
            nativeQuery = true)
    void upsertDriverLocation(@Param("username") String username,
                              @Param("lat") double lat,
                              @Param("lon") double lon);

    @Query(value = """
    SELECT username, lat, lon, distance
    FROM (
        SELECT username, lat, lon,
            ( 6371 * acos( cos( radians(:pickupLat) ) * cos( radians(lat) )
            * cos( radians(lon) - radians(:pickupLon) ) + sin( radians(:pickupLat) )
            * sin( radians(lat) ) ) ) AS distance
        FROM driver_locations
    ) AS calculated_distances
    WHERE distance <= :radius  AND distance >= 0.01
    ORDER BY distance ASC;
    """, nativeQuery = true)
    List<Object[]> findNearbyDriversRaw(@Param("pickupLat") double pickupLat,
                                        @Param("pickupLon") double pickupLon,
                                        @Param("radius") double radius);


    default List<DriverLocation> findNearbyDrivers(double pickupLat, double pickupLon, double radius) {

        List<Object[]> results = findNearbyDriversRaw(pickupLat, pickupLon, radius);

        return results.stream()
                .map(result ->
                DriverLocation.builder()
                        .username((String) result[0])
                        .lat((Double) result[1])
                        .lon((Double) result[2])
                        .distance((Double) result[3])
                        .build()
                )
                .toList();
    }

    Optional<DriverLocation> findByUsername(String username);

}
