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

import com.axiom.unrealdtoconsciousnpc.websockets.agent.AnnaAgentService;

import java.io.IOException;

// Unreal will connect to ws://localhost:8050/ws
@Component
@ServerEndpoint("/ws")


public class RawGameWebSocketEndpoint {

    private static AnnaAgentService annaAgentService;

    public static void setAnnaAgentService(AnnaAgentService service) {
        annaAgentService = service;
    }

    private static final Logger logger = LoggerFactory.getLogger(RawGameWebSocketEndpoint.class);

    @OnOpen
    public void onOpen(Session session) {
        logger.info(
                "RAW WS: Connection opened for session: {}",
                session.getId()
        );
    }


    @OnMessage
    public void onMessage(String message, Session session) {

        // Existing incoming Unreal event
        logger.info("RAW WS: Received message from session {}: {}",
                session.getId(), message);

        // NEW:
        // Ask Anna's agent what she thinks about this event.
        String annaResponse = annaAgentService.generateResponse(message);

        // NEW:
        // Spring console ONLY.
        logger.info("ANNA AGENT RESPONSE: {}", annaResponse);

        try {

            // LEAVE OUR KNOWN-GOOD RESPONSE ALONE.
            session.getBasicRemote().sendText(annaResponse);

            logger.info(
                    "RAW WS: Sent message back to Unreal: HELLO PLAYER"
            );

        } catch (IOException e) {

            logger.error(
                    "RAW WS: Error sending message back to Unreal: {}",
                    e.getMessage()
            );
        }
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


