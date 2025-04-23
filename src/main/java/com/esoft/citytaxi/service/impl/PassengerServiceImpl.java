package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.entity.Passenger;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.repository.PassengerRepository;
import com.esoft.citytaxi.service.EmailService;
import com.esoft.citytaxi.service.PassengerService;
import com.esoft.citytaxi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository repository;

    private final UserService userService;

    private final EmailService emailService;
    public PassengerServiceImpl(PassengerRepository repository, UserService userService, EmailService emailService) {
        this.repository = repository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public Passenger save(Passenger passenger) {
        User user = userService.createUser(passenger.getUser());
        passenger.setUser(user);

        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Registration Successful</title>" +
                "</head>" +
                "<body>" +
                "<h2>Welcome to Our Service!</h2>" +
                "<p>Dear " + passenger.getUser().getUsername() + ",</p>" +
                "<p>Congratulations! Your registration was successful. You can now access your account using the following details:</p>" +
                "<p><strong>Username:</strong> " + passenger.getUser().getUsername() + "</p>" +
                "<p><strong>Temporary Password:</strong> " + passenger.getUser().getPassword() + "</p>" +
                "<p>Please change your password upon your first login to ensure your account's security.</p>" +
                "<p>If you have any questions, feel free to reach out to us at <a href='mailto:support@citytaxi.com'>support@citytaxi.com</a>.</p>" +
                "<p>Best regards,</p>" +
                "<p>City Taxi Team</p>" +
                "</body>" +
                "</html>";

        emailService.send(user.getEmail(), "Registered Successfully", emailContent);
        return repository.save(passenger);
    }

    @Override
    public Passenger savePassenger(Passenger passenger) {
        return repository.save(passenger);
    }

    @Override
    public Passenger findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Passenger> findAll() {
        return this.repository.findAllNotStatus(UserStatuses.REMOVED);
    }

    @Override
    public Passenger findByUsername(String username)  {
        return this.repository.findByUserUsername(username)
                .orElse( null);
    }

}
