package com.planprostructure.planpro.payload.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketInfo {
    private String websocketEndpoint;
    private String stompEndpoint;
    private String applicationDestinationPrefix;
    private String userDestinationPrefix;
    private String topicPrefix;
    private String queuePrefix;
} 