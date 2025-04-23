package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.Operator;
import com.esoft.citytaxi.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface OperatorService {

    Operator findByUser(User user);

    Operator findByUserName(String username);

}
