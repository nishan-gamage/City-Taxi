package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.entity.Operator;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.repository.OperatorRepository;
import com.esoft.citytaxi.service.OperatorService;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImpl implements OperatorService {

    private final OperatorRepository operatorRepository;

    public OperatorServiceImpl(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Override
    public Operator findByUser(User user) {
        return operatorRepository.findByUser(user);
    }

    @Override
    public Operator findByUserName(String username) {
        return operatorRepository.findByUser_Username(username);
    }
}
