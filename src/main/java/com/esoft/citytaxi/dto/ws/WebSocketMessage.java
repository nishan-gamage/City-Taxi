package com.esoft.citytaxi.dto.ws;

import com.esoft.citytaxi.constant.WebSocketMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage<T> {

    private WebSocketMessageType type;

    private String description;

    private T data;

}
