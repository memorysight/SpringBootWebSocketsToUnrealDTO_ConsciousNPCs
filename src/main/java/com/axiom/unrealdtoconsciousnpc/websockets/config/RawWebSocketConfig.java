package com.axiom.unrealdtoconsciousnpc.websockets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class RawWebSocketConfig {

    public RawWebSocketConfig() {
        System.out.println("Raw Websocking is freaking loaded wtf");
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}