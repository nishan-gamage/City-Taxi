package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.transactioanal.DriverLocation;
import com.esoft.citytaxi.exception.ResourceNotFoundException;
import com.esoft.citytaxi.repository.DriverRepository;
import com.esoft.citytaxi.service.DriverLocationService;
import com.esoft.citytaxi.service.DriverService;
import com.esoft.citytaxi.service.EmailService;
import com.esoft.citytaxi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository repository;

    private final DriverLocationService driverLocationService;

    private final UserService userService;

    private final EmailService emailService;

    public DriverServiceImpl(DriverRepository repository, DriverLocationService driverLocationService, UserService userService, EmailService emailService) {
        this.repository = repository;
        this.driverLocationService = driverLocationService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public Driver save(Driver driver) {
        User user = userService.createUser(driver.getUser());
        driver.setUser(user);
        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Registration Successful</title>" +
                "</head>" +
                "<body>" +
                "<h2>Welcome to Our Service!</h2>" +
                "<p>Dear " + driver.getUser().getUsername() + ",</p>" +
                "<p>Congratulations! Your registration was successful. You can now access your account using the following details:</p>" +
                "<p><strong>Username:</strong> " + driver.getUser().getUsername() + "</p>" +
                "<p><strong>Temporary Password:</strong> " + driver.getUser().getPassword() + "</p>" +
                "<p>Please change your password upon your first login to ensure your account's security.</p>" +
                "<p>If you have any questions, feel free to reach out to us at <a href='mailto:support@citytaxi.com'>support@citytaxi.com</a>.</p>" +
                "<p>Best regards,</p>" +
                "<p>City Taxi Team</p>" +
                "</body>" +
                "</html>";


        emailService.send(user.getEmail(), "Registered Successfully", emailContent);
        return repository.save(driver);
    }

    @Override
    public Driver update(Driver driver) {
        User user = userService.update(driver.getUser());
        driver.setUser(user);
        return repository.save(driver);
    }

    @Override
    public Driver findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Driver> findNearbyDrivers(double pickupLat, double pickupLon, double radius) {

        List<DriverLocation> nearbyDrivers = driverLocationService.findNearbyDrivers(pickupLat, pickupLon, radius);

        List<Driver> drivers = repository.findAllByUsernamesIgnoreCase(
                 nearbyDrivers.stream()
                .map(DriverLocation::getUsername)
                .collect(Collectors.toList()))
                .stream()
                .filter(d -> DriverStatuses.AVAILABLE.equals(d.getStatus()))
                .toList();

        Map<String, DriverLocation> driverLocationMap = nearbyDrivers.stream()
                .collect(Collectors.toMap(DriverLocation::getUsername, location -> location));

        drivers.forEach(driver -> driver.setLocation(driverLocationMap.get(driver.getUser().getUsername())));

        return drivers;
    }

    @Override
    public Driver getDriverDetailsByUsername(String username) throws ResourceNotFoundException {
        Driver driver = repository.findByUser_Username(username);
        if (driver == null) {
            throw new ResourceNotFoundException("Driver not found with username: " + username);
        }
        Optional<DriverLocation> driverLocation = driverLocationService.findByUsername(username);
        driverLocation.ifPresent(driver::setLocation);
        return driver;
    }

    @Override
    public List<Driver> findAll() {
        return repository.findAllNotStatus(UserStatuses.REMOVED);
    }

    @Override
    public Driver findByUsername(String username) {
        return repository.findByUser_Username(username);
    }
}
