package me.cizezsy.yourdrawiguess.net;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class MyWebSocketClient extends WebSocketClient {


    public MyWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    public MyWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public MyWebSocketClient(URI serverUri, Draft draft, Map<String, String> headers, int connectTimeout) {
        super(serverUri, draft, headers, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
