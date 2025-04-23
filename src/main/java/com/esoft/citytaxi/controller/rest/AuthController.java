package com.esoft.citytaxi.controller.rest;

import com.esoft.citytaxi.constant.UserRoles;
import com.esoft.citytaxi.constant.UserStatuses;
import com.esoft.citytaxi.dto.auth.request.AuthRequest;
import com.esoft.citytaxi.dto.auth.request.DriverRegistrationRequestDto;
import com.esoft.citytaxi.dto.auth.request.PassengerRegistrationRequestDto;
import com.esoft.citytaxi.dto.auth.response.AuthResponse;
import com.esoft.citytaxi.dto.general.AppResponse;
import com.esoft.citytaxi.entity.User;
import com.esoft.citytaxi.entity.VehicleType;
import com.esoft.citytaxi.service.*;
import com.esoft.citytaxi.util.auth.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final PassengerService passengerService;

    private final DriverService driverService;

    private final OperatorService operatorService;

    private final UserService userService;

    private  final VehicleService vehicleService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, PassengerService passengerService, DriverService driverService, OperatorService operatorService, UserService userService, VehicleService vehicleService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passengerService = passengerService;
        this.driverService = driverService;
        this.operatorService = operatorService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse<AuthResponse>> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {

        AppResponse<AuthResponse> response = new AppResponse<>();

        try {

            Optional<User> user = userService.findByUsername(authRequest.getUsername());

            if(user.isPresent() && user.get().getStatus() == UserStatuses.ACTIVE) {

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
                );

                final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

                AuthResponse authResponse = AuthResponse.builder()
                        .accessToken(jwtUtil.generateAccessToken(
                                userDetails,
                                userDetails.getAuthorities().toArray()[0].toString())
                        )
                        .name(getUserName(user))
                        .email(user.get().getEmail())
                        .username(user.get().getUsername())
                        .userRole(user.get().getRole().getCode())
                        .build();

                response.setStatusCode(200);
                response.setMessage("Success");
                response.setData(authResponse);

            } else {
                throw new Exception("Incorrect username or inactive account");
            }


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed : " + e.getMessage());
        }

        response.setLocalDateTime(LocalDateTime.now());

        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/passenger/sign-up")
    public ResponseEntity<AppResponse<String>> passengerSignUp(@RequestBody PassengerRegistrationRequestDto request) throws Exception {

        AppResponse<String> response = new AppResponse<>();

        try {

            Optional<User> user = userService.findByEmail(request.getEmail());

            if(user.isPresent()) {
                response.setStatusCode(400);
                response.setMessage("User exist");
            } else {
                passengerService.save(request.getPassenger());
                response.setStatusCode(201);
                response.setMessage("Success");
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed : " + e.getMessage());
        }

        response.setLocalDateTime(LocalDateTime.now());

       return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/driver/sign-up")
    public ResponseEntity<AppResponse<String>> driverSignUp(@RequestBody DriverRegistrationRequestDto request) throws Exception {
        AppResponse<String> response = new AppResponse<>();

        try {

            Optional<User> user = userService.findByEmail(request.getEmail());
            Optional<VehicleType> vehicleType = vehicleService.findVehicleTypeById(request.getVehicleTypeId());

            if(user.isPresent()) {
                response.setStatusCode(400);
                response.setMessage("User exist");
            } else if (vehicleType.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vehicle Type is not exist");
            } else {
                driverService.save(request.getDriver(vehicleType.get()));
                response.setStatusCode(201);
                response.setMessage("Success");
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Failed : " + e.getMessage());
        }

        response.setLocalDateTime(LocalDateTime.now());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    private String getUserName(Optional<User> user) {

        String name = null;

        if(UserRoles.PASSENGER.equals(user.get().getRole().getCode())) {
            name = passengerService.findByUser(user.get()).getName();
        } else if (UserRoles.DRIVER.equals(user.get().getRole().getCode())) {
            name = driverService.findByUser(user.get()).getName();
        } else if (UserRoles.OPERATOR.equals(user.get().getRole().getCode())) {
            name = operatorService.findByUser(user.get()).getName();
        }

        return name;
    }
}
