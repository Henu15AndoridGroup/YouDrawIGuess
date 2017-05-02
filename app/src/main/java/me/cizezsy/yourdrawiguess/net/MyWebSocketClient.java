package me.cizezsy.yourdrawiguess.net;

import android.app.Activity;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.model.WebSocketMessage;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;
import me.cizezsy.yourdrawiguess.util.JsonUtils;

public class MyWebSocketClient extends WebSocketClient {

    private PaintView mPaintView;
    private Activity mActivity;

    public MyWebSocketClient(URI serverURI, Activity activity) {
        super(serverURI);
        this.mActivity = activity;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
    }

    @Override
    public void onMessage(String message) {
        WebSocketMessage webSocketMessage = JsonUtils.fromJson(message, WebSocketMessage.class);
        String data = webSocketMessage.getData();
        switch (webSocketMessage.getType()) {
            case 0:
                break;
            case 1:
                Log.d("Num", data);
                break;
            case 2:
                Step step = JsonUtils.fromJson(data, Step.class);
                mActivity.runOnUiThread(() -> mPaintView.refreshPath(step));
                break;
            case 4:
                mPaintView.setToMe(true);
                break;
            case 5:
                Type stepListType = new TypeToken<ArrayList<Step>>(){}.getType();
                List<Step> stepList = JsonUtils.fromJson(data, stepListType);
                mActivity.runOnUiThread(() -> mPaintView.refreshPath(stepList));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

    public void setPaintView(PaintView paintView) {
        mPaintView = paintView;
    }
}
