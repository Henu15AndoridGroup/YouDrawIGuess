package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.cizezsy.yourdrawiguess.model.Room;

public class RoomListActivity extends AppCompatActivity {

    private List<Room> roomList = new ArrayList<>();
    int page;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initView();
    }


    private void initView() {
    }


    //TODO 刷新数据时调用
    private void onRefresh() {

    }

}
