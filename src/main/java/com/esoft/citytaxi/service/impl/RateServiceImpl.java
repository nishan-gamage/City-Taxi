package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Rates;
import com.esoft.citytaxi.repository.RateRepository;
import com.esoft.citytaxi.service.RateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateServiceImpl implements RateService {

    private final RateRepository repository;

    public RateServiceImpl(RateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Rates add(Rates rates) {
        return repository.save(rates);
    }

    @Override
    public List<Rates> findByDriver(Driver driver) {
        return repository.findRatesByDriver(driver);
    }

    @Override
    public List<Rates> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Rates> findByDriverUsername(String driverUsername) {
        return repository.findRatesByDriver_UserUsername(driverUsername);
    }
}
