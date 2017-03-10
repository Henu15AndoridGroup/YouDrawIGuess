package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.cizezsy.yourdrawiguess.model.Room;

public class RoomListActivity extends AppCompatActivity {

    List<Room> roomList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
    }


    private void initView() {

    }


    //TODO SwipeRefreshLayout的刷新事件调用此方法
    private void onRefresh() {

    }

}
