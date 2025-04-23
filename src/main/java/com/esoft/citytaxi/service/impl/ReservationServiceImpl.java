package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.constant.DriverStatuses;
import com.esoft.citytaxi.constant.ReservationStatus;
import com.esoft.citytaxi.constant.WebSocketMessageType;
import com.esoft.citytaxi.controller.ws.WebSocketHandler;
import com.esoft.citytaxi.dto.reservation.ReservationResponseDto;
import com.esoft.citytaxi.dto.ws.WebSocketMessage;
import com.esoft.citytaxi.entity.Driver;
import com.esoft.citytaxi.entity.Reservations;
import com.esoft.citytaxi.repository.ReservationRepository;
import com.esoft.citytaxi.service.DriverService;
import com.esoft.citytaxi.service.ReservationService;
import com.esoft.citytaxi.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;

    private final WebSocketHandler webSocketHandler;

    private final DriverService driverService;

    private final ObjectMapper objectMapper;

    private final SmsService smsService;

    public ReservationServiceImpl(ReservationRepository repository, WebSocketHandler webSocketHandler, DriverService driverService, ObjectMapper objectMapper, SmsService smsService) {
        this.repository = repository;
        this.webSocketHandler = webSocketHandler;
        this.driverService = driverService;
        this.objectMapper = objectMapper;
        this.smsService = smsService;
    }

    @Override
    public Reservations add(Reservations reservations) {
        Reservations res = repository.save(reservations);

        Driver driver = reservations.getDriver();
        driver.setStatus(DriverStatuses.BUSY);
        driverService.update(driver);

        if(Objects.nonNull(res.getOperator()) && Objects.nonNull(res.getOperator().getUser()) && StringUtils.isNoneBlank(res.getOperator().getUser().getUsername())) {
            notify(res.getOperator().getUser().getUsername(), WebSocketMessageType.OPERATOR_PICKUP_REQUEST_ACCEPTED, res);
        } else {
            notify(res.getPassenger().getUser().getUsername(), WebSocketMessageType.PICKUP_REQUEST_ACCEPTED, res);
            smsService.send(
                    res.getPassenger().getUser().getContactNumber(),
                    "Pickup Accepted : " + res.getDriver().getName() + " " + res.getDriver().getUser().getContactNumber()
            );
        }
        return res;
    }

    @Override
    public Reservations findByReservationId(String reservationId) {
        return repository.findReservationsByReservationId(reservationId);
    }

    @Override
    public Optional<Reservations> findActiveReservationForPassenger(String passengerUserName) {
        List<Reservations> reservations = repository.findReservationsByPassenger_User_UsernameAndStatusInOrderByIdDesc(passengerUserName,
                Arrays.asList(ReservationStatus.DRIVER_ACCEPTED, ReservationStatus.PICKED_UP, ReservationStatus.DROP_OFF));

        if(reservations != null && !reservations.isEmpty()) {
            return Optional.of(reservations.get(0));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Reservations> findActiveReservationForDriver(String driverUserName) {
        List<Reservations> reservations =  repository.findReservationsByDriver_User_UsernameAndStatusInOrderByIdDesc(driverUserName,
                Arrays.asList(ReservationStatus.DRIVER_ACCEPTED, ReservationStatus.PICKED_UP, ReservationStatus.DROP_OFF));

        if(reservations != null && !reservations.isEmpty()) {
            return Optional.of(reservations.get(0));
        }

        return Optional.empty();
    }

    @Override
    public Reservations update(Reservations reservations) {

        Reservations res = repository.save(reservations);

        if(reservations.getStatus() == ReservationStatus.COMPLETE) {
            Driver driver = reservations.getDriver();
            driver.setStatus(DriverStatuses.AVAILABLE);
            driverService.update(driver);
        }

        // notify to driver and passenger
        notify(res.getPassenger().getUser().getUsername(), WebSocketMessageType.STATUS_UPDATE, res);
        notify(res.getDriver().getUser().getUsername(), WebSocketMessageType.STATUS_UPDATE, res);

        return res;
    }

    @Override
    public List<Reservations> findAll() {
        return repository.findAllByOrderByIdDesc();
    }

    private void notify(String receiver, WebSocketMessageType webSocketMessageType, Reservations res) {
        try {

            WebSocketMessage<ReservationResponseDto> webSocketMessage = new WebSocketMessage<>();
            webSocketMessage.setType(webSocketMessageType);
            webSocketMessage.setDescription(webSocketMessageType.name());
            webSocketMessage.setData(new ReservationResponseDto(res));

            webSocketHandler.sendMessageToUser(
                    receiver,
                    objectMapper.writeValueAsString(webSocketMessage)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
