package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Rates;

import java.util.List;

public interface RateService {

    Rates add(Rates rates);

    List<Rates> findByDriver(Driver driver);

    List<Rates> findAll();

    List<Rates> findByDriverUsername(String driverUsername);
}
