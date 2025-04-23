package com.esoft.citytaxi.controller.ws;

import com.esoft.citytaxi.constant.WebSocketMessageType;
import com.esoft.citytaxi.dto.ws.WebSocketCommunicationRequest;
import com.esoft.citytaxi.dto.ws.WebSocketDriverLocationCaptureRequest;
import com.esoft.citytaxi.dto.ws.WebSocketMessage;
import com.esoft.citytaxi.entity.transactioanal.DriverLocation;
import com.esoft.citytaxi.service.DriverLocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    public static final String TYPE = "type";
    public static final String USER_ID = "userId";
    public static final String USER_ID_WITH_EQUALS = "userId=";

    private final Map<String, WebSocketSession> wsSessions = new ConcurrentHashMap<>();

    private final DriverLocationService driverLocationService;

    private final ObjectMapper objectMapper;

    public WebSocketHandler(DriverLocationService driverLocationService, ObjectMapper objectMapper) {
        this.driverLocationService = driverLocationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {

        // Extract the type
        WebSocketMessageType type = getWebSocketMessageType(message);
        String username = getUserIdFromQuery(session.getUri().getQuery());

        if (WebSocketMessageType.INSERT_DRIVER_LOCATION.equals(type)) {

            // Deserialize the incoming JSON message
            WebSocketMessage<WebSocketDriverLocationCaptureRequest> webSocketMessage =
                    objectMapper.readValue(
                            message.getPayload(),
                            objectMapper.getTypeFactory().constructParametricType(WebSocketMessage.class,
                                    WebSocketDriverLocationCaptureRequest.class)
                    );


            WebSocketDriverLocationCaptureRequest locationRequest = webSocketMessage.getData();
            driverLocationService.save(
                    DriverLocation.builder()
                            .username(username)
                            .lat(locationRequest.getLat())
                            .lon(locationRequest.getLon())
                            .build()
            );

        } else if (WebSocketMessageType.PICKUP_REQUEST.equals(type) || WebSocketMessageType.PICKUP_REQUEST_ACCEPTED.equals(type)) {

            // Deserialize the incoming JSON message
            WebSocketMessage<WebSocketCommunicationRequest> webSocketMessage =
                    objectMapper.readValue(
                            message.getPayload(),
                            objectMapper.getTypeFactory().constructParametricType(WebSocketMessage.class,
                                    WebSocketCommunicationRequest.class)
                    );

            sendMessageToUser(webSocketMessage.getData().getReceiverUsername(), message.getPayload());
        } else if(WebSocketMessageType.RESERVED_DRIVER_LOCATION.equals(type)) {
            WebSocketMessage<WebSocketDriverLocationCaptureRequest> webSocketMessage =
                    objectMapper.readValue(
                            message.getPayload(),
                            objectMapper.getTypeFactory().constructParametricType(WebSocketMessage.class,
                                    WebSocketDriverLocationCaptureRequest.class)
                    );
            sendMessageToUser(webSocketMessage.getData().getPassengerUsername(), message.getPayload());
        } else if(WebSocketMessageType.OPERATOR_UPDATE_DRIVER_LOCATION.equals(type)) {
            WebSocketMessage<WebSocketDriverLocationCaptureRequest> webSocketMessage =
                    objectMapper.readValue(
                            message.getPayload(),
                            objectMapper.getTypeFactory().constructParametricType(WebSocketMessage.class,
                                    WebSocketDriverLocationCaptureRequest.class)
                    );
            if(Objects.isNull(wsSessions.get(webSocketMessage.getData().getDriverUsername()))) {
                WebSocketDriverLocationCaptureRequest locationRequest = webSocketMessage.getData();
                driverLocationService.save(
                        DriverLocation.builder()
                                .username(locationRequest.getDriverUsername())
                                .lat(locationRequest.getLat())
                                .lon(locationRequest.getLon())
                                .build()
                );
            }
            sendMessageToUser(webSocketMessage.getData().getDriverUsername(), message.getPayload());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = getUserIdFromQuery(session.getUri().getQuery());

        if (username != null) {
            wsSessions.put(username, session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        wsSessions.values().removeIf(s -> s.equals(session));
    }

    public void sendMessageToUser(String username, Object message) throws Exception {

        WebSocketSession session = wsSessions.get(username);

        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message.toString()));
        }

    }

    private String getUserIdFromQuery(String query) {
        if (query != null && query.contains(USER_ID)) {
            return query.split(USER_ID_WITH_EQUALS)[1];
        }
        return null;
    }

    private WebSocketMessageType getWebSocketMessageType(TextMessage message) throws JsonProcessingException {
        return WebSocketMessageType.valueOf((String) objectMapper.readValue(message.getPayload(), Map.class).get(TYPE));
    }

}
