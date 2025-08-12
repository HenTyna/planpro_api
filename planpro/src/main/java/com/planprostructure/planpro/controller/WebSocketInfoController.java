// package com.planprostructure.planpro.controller;

// import com.planprostructure.planpro.components.common.api.ProPlanRestController;
// import com.planprostructure.planpro.payload.websocket.WebSocketInfo;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api/wb/v1/websocket")
// @Tag(name = "WebSocket", description = "WebSocket connection information and endpoints")
// public class WebSocketInfoController extends ProPlanRestController {

//     @GetMapping("/info")
//     @Operation(summary = "Get WebSocket connection information", 
//                description = "Returns WebSocket endpoints and subscription topics for real-time chat")
//     public Object getWebSocketInfo() throws Throwable {
//         WebSocketInfo info = WebSocketInfo.builder()
//                 .websocketEndpoint("/ws")
//                 .stompEndpoint("/ws")
//                 .applicationDestinationPrefix("/app")
//                 .userDestinationPrefix("/user")
//                 .topicPrefix("/topic")
//                 .queuePrefix("/queue")
//                 .build();
        
//         return ok(info);
//     }
// } 