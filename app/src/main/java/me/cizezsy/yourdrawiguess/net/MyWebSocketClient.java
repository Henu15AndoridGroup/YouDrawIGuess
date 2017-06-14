package me.cizezsy.yourdrawiguess.net;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.util.Charsetfunctions;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.cizezsy.yourdrawiguess.model.Chat;
import me.cizezsy.yourdrawiguess.model.Grade;
import me.cizezsy.yourdrawiguess.model.Phrase;
import me.cizezsy.yourdrawiguess.model.message.FromServerMessage;
import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.ui.activity.GameActivity;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;
import me.cizezsy.yourdrawiguess.util.JsonUtils;
import me.cizezsy.yourdrawiguess.util.ToastUtils;

public class MyWebSocketClient extends WebSocketClient {
    private final static int NONE = 0;
    private final static int PLAYER_CHANGE = 1;
    private final static int DRAW = 2;
    private final static int START = 3;
    private final static int TO_ME = 4;
    private final static int RESUME = 5;
    private final static int CHAT = 6;
    private final static int PHRASE = 7;
    private final static int STOP = 8;
    private final static int INFORM = 9;
    private final static int OVER_TIME_INFORM = 10;
    private final static int INFO = 11;

    private PaintView mPaintView;
    private Activity mActivity;
    private StringBuilder frameValue = new StringBuilder();
    private AlertDialog alertDialog;

    private List<Chat> mChatList = new ArrayList<>();
    private TimerTask gameStopTask;

    public MyWebSocketClient(URI serverURI, Map<String, String> header, Activity activity) {
        super(serverURI, new Draft_17(), header, 12000);
        this.mActivity = activity;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        Log.d("webSocket", "open");
        mActivity.runOnUiThread(() -> {
            ((GameActivity) mActivity).hiddenProgressBar();
            mPaintView.setEnabled(true);
        });
    }

    @Override
    public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
        try {
            frameValue.append(Charsetfunctions.stringUtf8(frame.getPayloadData()));
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
        if (frame.isFin()) {
            onMessage(frameValue.toString());
            frameValue = new StringBuilder();
        }
    }

    @Override
    public void onMessage(String message) {
        FromServerMessage fromServerMessage = JsonUtils.fromJson(message, FromServerMessage.class);
        JsonElement data = fromServerMessage.getData();
        Log.d("webSocket", "message" + data);
        try {
            switch (fromServerMessage.getType()) {
                case NONE:
                    break;
                case PLAYER_CHANGE:
                    receiverPlayerNumber(data);
                    break;
                case DRAW:
                    receiveDrawMessage(data);
                    break;
                case START:
                    receiveStartMessage(data);
                    break;
                case TO_ME:
                    mPaintView.setToMe(true);
                    break;
                case RESUME:
                    receiveResumeMessage(data);
                    break;
                case CHAT:
                    receiveChatMessage(data);
                    break;
                case PHRASE:
                    receivePhraseMessage(data);
                    break;
                case STOP:
                    receiveStopMessage(data);
                    break;
                case INFORM:
                    receiveInformMessage(data);
                    break;
                case OVER_TIME_INFORM:
                    receiveOverTimeInformMessage(data);
                    break;
                case INFO:
                    receivePhraseInfoMessage(data);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receivePhraseInfoMessage(JsonElement data) {
        String info = JsonUtils.fromJson(data, String.class);
        mActivity.runOnUiThread(() -> {
            if (info.endsWith("个字")) {
                if (gameStopTask != null) {
                    gameStopTask.cancel();
                }
                gameStopTask = new TimerTask() {
                    int time = 120;

                    @Override
                    public void run() {
                        mActivity.runOnUiThread(() ->
                                ((GameActivity) mActivity).setGameInfo(String.format(Locale.CHINA, "%s 游戏还剩%d秒", info, time--)));
                    }
                };
                new Timer().schedule(gameStopTask, 0, 1000);
            } else {
                ((GameActivity) mActivity).setGameInfo(info);
                Chat chat = new Chat();
                chat.setUsername("系统");
                chat.setContent(info);
                chat.setType(Chat.Type.SYSTEM);
                mChatList.add(chat);
                mActivity.runOnUiThread(()->{
                    ((GameActivity) mActivity).notifyChatAdd();
                });
            }
        });
    }

    private void receiveOverTimeInformMessage(JsonElement data) {
        Phrase phrase = JsonUtils.fromJson(data, Phrase.class);
        mActivity.runOnUiThread(() -> {
            ToastUtils.makeShortText(String.format(Locale.CHINA, "超时系统自动选择%s", phrase.getName()), mActivity);
            ((GameActivity) mActivity).setGameInfo(String.format(Locale.CHINA, "已选：%s", phrase.getName()));
        });
    }

    private void receiveInformMessage(JsonElement data) {
        Phrase phrase = JsonUtils.fromJson(data, Phrase.class);
        mActivity.runOnUiThread(() -> {
            ToastUtils.makeShortText("选择成功", mActivity);
            gameStopTask = new TimerTask() {
                int time = 120;
                @Override
                public void run() {
                    mActivity.runOnUiThread(() ->
                            ((GameActivity) mActivity).setGameInfo(String.format(Locale.CHINA, "已选：%s 还剩%d秒", phrase.getName(), time--)));
                }
            };
            new Timer().schedule(gameStopTask, 0, 1000);
        });
    }

    private void receiveStopMessage(JsonElement data) {
        if (gameStopTask != null) {
            gameStopTask.cancel();
            gameStopTask = null;
        }
        List<Grade> gradeList = JsonUtils.fromJson(data, new TypeToken<List<Grade>>() {
        }.getType());
        StringBuilder sb = new StringBuilder();
        for (Grade g : gradeList) {
            sb.append(g.getUsername()).append(": ").append(g.getGrade()).append("分\n");
        }
        Chat chat = new Chat();
        chat.setUsername("系统");
        chat.setContent(sb.toString());
        chat.setType(Chat.Type.SYSTEM);
        mChatList.add(chat);
        mActivity.runOnUiThread(() -> {
            alertDialog = new AlertDialog.Builder(mActivity)
                    .setTitle("游戏结束，即将自动开始下一轮")
                    .setMessage(chat.getContent())
                    .setCancelable(false)
                    .create();
            alertDialog.show();
            mPaintView.reset();
            ((GameActivity) mActivity).setPlayerMessage(chat);
            ((GameActivity) mActivity).notifyChatAdd();
            ((GameActivity) mActivity).setGameInfo("游戏结束");
        });
        mPaintView.setToMe(false);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("webSocket", "close");
    }

    @Override
    public void onError(Exception ex) {
        Log.d("webSocket", "error: " + ex.getMessage());
    }


    private void receiveChatMessage(JsonElement data) {
        Chat chat = JsonUtils.fromJson(data, Chat.class);
        mChatList.add(chat);
        mActivity.runOnUiThread(() -> {
            ((GameActivity) mActivity).setPlayerMessage(chat);
            ((GameActivity) mActivity).notifyChatAdd();
        });
    }

    private void receiveResumeMessage(JsonElement data) {
        Type stepListType = new TypeToken<ArrayList<Step>>() {
        }.getType();
        List<Step> stepList = JsonUtils.fromJson(data, stepListType);
        mActivity.runOnUiThread(() -> mPaintView.refreshPath(stepList));
    }

    private void receiveDrawMessage(JsonElement data) {
        Step step = JsonUtils.fromJson(data, Step.class);
        mActivity.runOnUiThread(() -> mPaintView.refreshPath(step));
    }

    private void receiverPlayerNumber(JsonElement data) {
        Integer playerNum = JsonUtils.fromJson(data, Integer.class);
        mActivity.runOnUiThread(() -> ((GameActivity) mActivity).setPlayerNumber(playerNum));
        Log.d("webSocket", "Player number: " + playerNum);
    }

    private void receiveStartMessage(JsonElement data) {
        String startInfo = JsonUtils.fromJson(data, String.class);
        mActivity.runOnUiThread(() -> {
            ((GameActivity) mActivity).setGameInfo(startInfo);
            ToastUtils.makeShortText(startInfo, mActivity);
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog = null;
            }
        });
    }

    private void receivePhraseMessage(JsonElement data) {
        List<Phrase> phraseList = JsonUtils.fromJson(data, new TypeToken<List<Phrase>>() {
        }.getType());
        mActivity.runOnUiThread(() -> ((GameActivity) mActivity).startSelectPhrase(phraseList));
    }

    public List<Chat> getChatList() {
        return mChatList;
    }

    public void setPaintView(PaintView paintView) {
        mPaintView = paintView;
    }

}
