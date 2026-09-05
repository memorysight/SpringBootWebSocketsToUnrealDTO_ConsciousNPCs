package com.axiom.unrealdtoconsciousnpc.websockets.dto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

// Unreal will connect to ws://localhost:8050/ws
@Component
@ServerEndpoint("/ws")


public class RawGameWebSocketEndpoint {


    private static final Logger logger = LoggerFactory.getLogger(RawGameWebSocketEndpoint.class);

    @OnOpen
    public void onOpen(Session session) {
        logger.info("RAW WS: Connection opened for session: {}", session.getId());
        // Optional: Send a welcome message back to the client
        try {
            session.getBasicRemote().sendText("Welcome to the raw game WebSocket!");
        } catch (IOException e) {
            logger.error("Error sending welcome message: {}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("RAW WS: Received message from session {}: {}", session.getId(), message);

        // Here you would parse your incoming message.
        // For MVP, you're expecting something like "H_PRESSED" or a JSON string.
        if (message.contains("H_PRESSED")) { // Simple check for now
            logger.info("RAW WS: 'H' button press detected from client!");
            // Optionally send a confirmation back to Unreal
            try {
                session.getBasicRemote().sendText("Server received: H_PRESSED confirmation!");
            } catch (IOException e) {
                logger.error("Error sending H_PRESSED confirmation: {}", e.getMessage());
            }
        }
        // You can deserialize JSON here if your Unreal client sends JSON strings
        // ObjectMapper mapper = new ObjectMapper();
        // GameStateUpdateMessage update = mapper.readValue(message, GameStateUpdateMessage.class);
        // logger.info("Parsed DTO: {}", update.getPlayerId());
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("RAW WS: Connection closed for session: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("RAW WS: Error for session {}: {}", session.getId(), error.getMessage());
        error.printStackTrace(); // Log the full stack trace for debugging
    }

    public RawGameWebSocketEndpoint() {
        System.out.println("RawGameWebSocketEndpoint Fn Created!!");
    }


}


