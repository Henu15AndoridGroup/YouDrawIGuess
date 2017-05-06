package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.PlayerMessage;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;
import me.cizezsy.yourdrawiguess.net.YdigRetrofit;
import me.cizezsy.yourdrawiguess.ui.widget.CleanEditText;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;
import me.cizezsy.yourdrawiguess.util.JsonUtils;
import okhttp3.Cookie;

public class GameActivity extends AppCompatActivity {

    private static final String SOCKET_SERVER_URL = "ws://115.159.49.186:8080/ydig2/draw";

    private MyWebSocketClient client;

    @BindView(R.id.pv_main)
    PaintView mPaintView;

    @BindView(R.id.pb_game)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_player_num)
    TextView mPlayerTv;

    @BindView(R.id.tv_player_mes)
    TextView mMessageTv;

    @BindView(R.id.et_chat_message)
    CleanEditText mChatEt;

    @BindView(R.id.btn_send_chat_mes)
    Button mMessageBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        List<Cookie> cookieList = YdigRetrofit.cookieStore;
        StringBuilder sb = new StringBuilder();
        cookieList.forEach(c -> sb.append(c.toString()));
        Map<String, String> cookie = new HashMap<>();
        cookie.put("Cookie", sb.toString());
        client = new MyWebSocketClient(URI.create(SOCKET_SERVER_URL), cookie, this);

        mPaintView.setClient(client);
        mPaintView.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        client.connect();

        mMessageBtn.setOnClickListener(v -> {
            String chatMes = mChatEt.getText().toString();
            if (TextUtils.isEmpty(chatMes))
                return;
            PlayerMessage message = new PlayerMessage<>(PlayerMessage.Type.MESSAGE, chatMes);
            client.send(JsonUtils.toJson(message));
        });
    }


    //TODO 退出游戏时的销毁逻辑
    @Override
    protected void onStop() {
        super.onStop();
        client.close();
    }

    //TODO 监听Back键事件， 弹出对话框，拦截退出请求。

    public void hiddenProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void setPlayerNumber(int peopleNumber) {
        mPlayerTv.setText("当前游戏人数 :" + peopleNumber);
    }

    public void setPlayerMessage(String message) {
        mMessageTv.setText(message);
    }
}
