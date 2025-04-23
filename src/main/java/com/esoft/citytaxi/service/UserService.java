package com.esoft.citytaxi.service;

import com.esoft.citytaxi.entity.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User update(User user);

    Optional<User> findByEmail(String email);

    Optional<User>  findByUsername(String username);

}
