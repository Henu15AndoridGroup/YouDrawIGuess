package me.cizezsy.yourdrawiguess.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.adapter.RoomRecyclerViewAdapter;
import me.cizezsy.yourdrawiguess.model.Room;
import me.cizezsy.yourdrawiguess.net.YdigRetrofitFactory;
import me.cizezsy.yourdrawiguess.util.JsonUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoomListActivity extends AppCompatActivity {

    @BindView(R.id.rv_room)
    RecyclerView mRoomListRv;
    @BindView(R.id.cl_room)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.pb_room)
    ProgressBar mProgressBar;

    private RoomRecyclerViewAdapter mAdapter;
    private List<Room> mRoomList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mAdapter = new RoomRecyclerViewAdapter(mRoomList, this);
        mRoomListRv.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRoomListRv.setLayoutManager(layoutManager);
        mRoomListRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        showProgressBar();
        YdigRetrofitFactory.getService()
                .roomList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usedForHttpMessage -> {
                    hideProgressBar();
                    if (usedForHttpMessage.getStatusCode() != 200)
                        return;
                    List<Room> roomListFromServer = JsonUtils.fromJson(usedForHttpMessage.getData().toString(),
                            new TypeToken<ArrayList<Room>>() {
                            }.getType());
                    mRoomList.addAll(roomListFromServer);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    hideProgressBar();
                    throwable.printStackTrace();
                    Log.e("roomList", throwable.getMessage());
                });
    }



    public void snackMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
