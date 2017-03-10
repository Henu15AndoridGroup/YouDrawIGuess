package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URI;

import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;

public class GameActivity extends AppCompatActivity{

    private static final String SOCKET_SERVER_URL = "";

    private MyWebSocketClient client;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        client = new MyWebSocketClient(URI.create(SOCKET_SERVER_URL));
        client.connect();
    }


    //TODO 退出游戏时的销毁逻辑
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //TODO 监听Back键事件， 弹出对话框，拦截退出请求。
}
