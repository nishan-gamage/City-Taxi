package com.esoft.citytaxi.config;

import com.esoft.citytaxi.controller.ws.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler locationWebSocketHandler;

    public WebSocketConfig(WebSocketHandler locationWebSocketHandler) {
        this.locationWebSocketHandler = locationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(locationWebSocketHandler, "/city-taxi").setAllowedOrigins("*");
    }

}