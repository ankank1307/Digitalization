package com.cncdigitalizationplatform.cncdigitalizationplatform.config;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.cncdigitalizationplatform.cncdigitalizationplatform.service.machineService.MachineDataParserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FanucWebSocketClient extends WebSocketClient {
    private final ObjectMapper objectMapper;
    private final MachineDataParserService machineDataParserService;

    @Autowired
    private MachineDataWebsocketHandler webSocketHandler;

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        System.out.println("WebSocket closed: " + arg1);
    }

    public FanucWebSocketClient(ObjectMapper objectMapper, MachineDataParserService machineDataParserService,
            MachineDataWebsocketHandler webSocketHandler)
            throws Exception {
        // super(new
        // URI("ws://localhost:8081/ws/data-points/postman_client?interval=1"));
        // super(new
        // URI("wss://websocket-production-edc2.up.railway.app/ws/data-points1/1"));
        super(new URI("wss://websocket-production-edc2.up.railway.app/ws/data-points/2"));
        // super(new URI("ws://localhost:8081/ws/data-points/2"));
        this.objectMapper = objectMapper;
        this.machineDataParserService = machineDataParserService;
        this.webSocketHandler = webSocketHandler;

    }

    @EventListener(ApplicationReadyEvent.class)
    public void startClient() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                this.connectBlocking();
            } catch (InterruptedException e) {
                System.err.println("WebSocket connection failed: " + e.getMessage());
            }
        });
    }

    @Override
    public void onError(Exception arg0) {
        System.err.println("WebSocket error: " + arg0.getMessage());
    }

    @Override
    public void onMessage(String arg0) {
        // System.out.println("Received: " + arg0);
        try {
            List<MachineConfig> mConfigs = machineDataParserService.returnStatus(arg0);
            webSocketHandler.broadcastData(mConfigs);
            machineDataParserService.parseData(arg0);

        } catch (Exception e) {
            System.err.println("Failed to parse or save: " + e.getMessage());
        }
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        System.out.println("WebSocket opened");
        send("[\"fanuc_status_run\", \"fanuc_status_run2\"]"); // Send initial subscription message
    }

}
