package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;

public class GameActivity extends AppCompatActivity{

    private static final String SOCKET_SERVER_URL = "ws://115.159.49.186:8080/ydig2/draw";

    private MyWebSocketClient client;

    @BindView(R.id.pv_main)
    PaintView mPaintView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        client = new MyWebSocketClient(URI.create(SOCKET_SERVER_URL), this);
        client.connect();
        mPaintView.setClient(client);
    }


    //TODO 退出游戏时的销毁逻辑
    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }

    //TODO 监听Back键事件， 弹出对话框，拦截退出请求。
}
