package com.esoft.citytaxi.service.impl;

import com.esoft.citytaxi.service.SmsService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${sms.account.enabled}")
    public Boolean ACCOUNT_ENABLED;

    @Value("${sms.infobip.api-key}")
    public String API_KEY;

    @Value("${sms.infobip.from-number}")
    public String FROM_NUMBER;

    private static final String INFOBIP_URL = "https://lq2jyr.api.infobip.com/sms/2/text/advanced";

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Override
    public void send(String toNumber, String content) {
        if (!ACCOUNT_ENABLED) {
            System.out.println("SMS sending is disabled.");
            return;
        }

        try {
            String formattedToNumber = formatToInternational(toNumber);

            // Construct JSON payload
            String jsonPayload = "{\"messages\":[{\"destinations\":[{\"to\":\"" + formattedToNumber + "\"}],"
                    + "\"from\":\"" + FROM_NUMBER + "\","
                    + "\"text\":\"" + content + "\"}]}";

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonPayload);

            // Build HTTP request with OkHttp
            Request request = new Request.Builder()
                    .url(INFOBIP_URL)
                    .method("POST", body)
                    .addHeader("Authorization", "App " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();

            // Execute request
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                System.out.println("SMS sent successfully to " + toNumber);
            } else {
                System.out.println("Failed to send SMS: " + response.message());
            }
            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatToInternational(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");

        if (phoneNumber.startsWith("94")) {
            return "+94" + phoneNumber.substring(2);
        } else if (phoneNumber.startsWith("0")) {
            return "+94" + phoneNumber.substring(1);
        } else {
            return "+94" + phoneNumber;
        }
    }
}
