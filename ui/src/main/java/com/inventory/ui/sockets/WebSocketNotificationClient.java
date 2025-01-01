package com.inventory.ui.sockets;

import lombok.Setter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

@Setter
public class WebSocketNotificationClient extends WebSocketClient {
    private Consumer<String> onMessageReceived;

    public WebSocketNotificationClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Notification received: " + message);
        if (onMessageReceived != null) {
            onMessageReceived.accept(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}