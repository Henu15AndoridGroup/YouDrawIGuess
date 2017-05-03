package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;

public class GameActivity extends AppCompatActivity {

    private static final String SOCKET_SERVER_URL = "ws://115.159.49.186:8080/ydig2/draw";

    private MyWebSocketClient client;

    @BindView(R.id.pv_main)
    PaintView mPaintView;

    @BindView(R.id.pb_game)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_player_num)
    TextView mPlayerTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        client = new MyWebSocketClient(URI.create(SOCKET_SERVER_URL), this);
        mPaintView.setClient(client);
        mPaintView.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        client.connect();
    }


    //TODO 退出游戏时的销毁逻辑
    @Override
    protected void onStop() {
        super.onDestroy();
        client.close();
    }

    //TODO 监听Back键事件， 弹出对话框，拦截退出请求。

    public void hiddenProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void setPlayerNumber(int peopleNumber) {
        mPlayerTv.setText("当前游戏人数" + peopleNumber);
    }
}
