package com.cncdigitalizationplatform.cncdigitalizationplatform.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.MachineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MachineDataWebsocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> subscriptions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    @Autowired
    MachineRepository machineRepository;

    public MachineDataWebsocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String clientId = extractClientId(session);
        sessions.put(clientId, session);
        System.out.println("WebSocket connection established for client: " + clientId);
        List<MachineConfig> mConfigs = new ArrayList<>();
        List<Machine> machines = machineRepository.findAll();

        for (Machine machine : machines) {
            MachineConfig config = new MachineConfig(machine.getId(), machine.getStatus());
            mConfigs.add(config);

        }
        String jsonData = objectMapper.writeValueAsString(mConfigs);
        session.sendMessage(new TextMessage(jsonData));

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String clientId = extractClientId(session);
        sessions.remove(clientId);
        subscriptions.remove(clientId);
        System.out.println("WebSocket connection closed for client: " + clientId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientId = extractClientId(session);
        String payload = message.getPayload();

        try {
            // Parse subscription message like ["fanuc_status_run", "fanuc_status_run2"]
            List<String> topics = objectMapper.readValue(payload, List.class);
            subscriptions.put(clientId, new HashSet<>(topics));
            System.out.println("Client " + clientId + " subscribed to: " + topics);
        } catch (Exception e) {
            System.err.println("Error parsing subscription message: " + e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String clientId = extractClientId(session);
        System.err.println("WebSocket error for client " + clientId + ": " + exception.getMessage());
    }

    private String extractClientId(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    // Method to broadcast data to all connected clients
    public void broadcastData(List<MachineConfig> data) {
        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            System.err.println("Error serializing data: " + e.getMessage());
            return;
        }

        sessions.forEach((clientId, session) -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonData));
                } catch (Exception e) {
                    System.err.println("Error sending message to client " + clientId + ": " +
                            e.getMessage());
                }
            }
        });
    }

    // Method to send data to specific client
    // public void sendToClient(String clientId, DataMessage data) {
    // WebSocketSession session = sessions.get(clientId);
    // if (session != null && session.isOpen()) {
    // try {
    // String jsonData = objectMapper.writeValueAsString(data);
    // session.sendMessage(new TextMessage(jsonData));
    // } catch (Exception e) {
    // System.err.println("Error sending message to client " + clientId + ": " +
    // e.getMessage());
    // }
    // }
    // }
}