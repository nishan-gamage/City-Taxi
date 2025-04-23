package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rates, Long> {

    List<Rates> findRatesByDriver(Driver driver);

    List<Rates> findRatesByDriver_UserUsername(String driverUsername);

}
