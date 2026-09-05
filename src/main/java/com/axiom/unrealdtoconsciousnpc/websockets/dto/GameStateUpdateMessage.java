package com.axiom.unrealdtoconsciousnpc.websockets.dto;

import lombok.Data;

@Data
public class GameStateUpdateMessage {
    private String playerId;
    private GameStateEventType eventType;
    private GameStateName stateName;
    private Long timestamp; // Unix epoch milliseconds from C++
    private Float duration; // Total duration of the state, if applicable
    private Float remainingDuration; // Current remaining time, if applicable
    private Float value;    // e.g., Kill Streak count for Hyperdrive, Shield Health
    private String description; // Any specific details for complex events
}