package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.UserRole;
import com.esoft.citytaxi.repository.UserRepository;
import com.esoft.citytaxi.repository.UserRoleRepository;
import com.esoft.citytaxi.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        // Create and save the userForCreation
        User userForCreation = new User();
        userForCreation.setUsername(user.getUsername());
        userForCreation.setEmail(user.getEmail());
        userForCreation.setPassword(passwordEncoder.encode(user.getPassword()));
        userForCreation.setContactNumber(user.getContactNumber());
        userForCreation.setStatus(UserStatuses.ACTIVE);

        Optional<UserRole> userRole = userRoleRepository.findByCode(user.getRole().getCode());
        userRole.ifPresent(userForCreation::setRole);

        return userRepository.save(userForCreation);
    }

    @Override
    public User update(User user) {
        // Create and save the userForCreation
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
