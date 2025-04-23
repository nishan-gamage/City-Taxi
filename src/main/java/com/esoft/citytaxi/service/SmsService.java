package com.esoft.citytaxi.service;

public interface SmsService {

    void send(String toNumber, String message);

}
