package com.esoft.citytaxi.service;

public interface EmailService {

    void send(String toEmail, String subject, String content);

}
