package com.esoft.citytaxi.constant;

public enum WebSocketMessageType {

    INSERT_DRIVER_LOCATION("Insert drivers' locations."),
    PICKUP_REQUEST("Sending pickup request to driver."),
    PICKUP_REQUEST_ACCEPTED("Sending pickup acceptance notification to passenger."),
    OPERATOR_PICKUP_REQUEST_ACCEPTED("Sending pickup acceptance notification to operator."),
    RESERVATION_PERSISTED("Reservation persisted"),
    STATUS_UPDATE("Status Update"),
    RESERVED_DRIVER_LOCATION("Driver live location of reservation"),
    OPERATOR_UPDATE_DRIVER_LOCATION("Operator update driver location");

    final String description;

    WebSocketMessageType(String description) {
        this.description = description;
    }
}
