package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.account.enabled}")
    public Boolean ACCOUNT_ENABLED;

    @Value("${email.account.username}")
    public String USERNAME;

    @Value("${email.account.password}")
    public String PASSWORD;

    @Value("${email.account.from-email}")
    public String FROM_EMAIL;

    @Override
    public void send(String toEmail, String subject, String content) {
        try {
            if (ACCOUNT_ENABLED) {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                // Set content as HTML
                message.setContent(content, "text/html; charset=utf-8");

                Transport.send(message);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
