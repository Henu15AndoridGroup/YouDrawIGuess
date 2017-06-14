package me.cizezsy.yourdrawiguess.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.Room;
import me.cizezsy.yourdrawiguess.net.YdigRetrofitFactory;
import me.cizezsy.yourdrawiguess.ui.activity.GameActivity;
import me.cizezsy.yourdrawiguess.ui.activity.RoomListActivity;
import me.cizezsy.yourdrawiguess.util.JsonUtils;
import me.cizezsy.yourdrawiguess.util.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RoomRecyclerViewAdapter.MyViewHold> {

    private List<Room> mRoomList;
    private Context mContext;

    public RoomRecyclerViewAdapter(List<Room> roomList, Context context) {
        mRoomList = roomList;
        mContext = context;
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_room, viewGroup, false);
        return new MyViewHold(v);
    }

    @Override
    public void onBindViewHolder(MyViewHold viewHold, int i) {
        Room room = mRoomList.get(i);
        viewHold.roomIdTv.setText(String.format(Locale.CHINA, "房间 %3d", room.getRoomId()));
        viewHold.roomTypeTv.setText(room.getRoomId() == 0 ? "公共画房" : "你画我猜");
        viewHold.roomStateTv.setText(String.format(Locale.CHINA, "人数%d/%d", room.getCurrentPlayer(), room.getMaxPlayer()));
        viewHold.itemView.setOnClickListener(v -> {
            if (!(mContext instanceof RoomListActivity)) {
                return;
            }
            RoomListActivity roomListActivity = ((RoomListActivity) mContext);
            roomListActivity.showProgressBar();
            YdigRetrofitFactory.getService()
                    .enterRoom(room.getRoomId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(usedForHttpMessage -> {
                        roomListActivity.hideProgressBar();
                        if (usedForHttpMessage.getStatusCode() == 200) {
                            Intent intent = new Intent(roomListActivity, GameActivity.class);
                            intent.putExtra("roomId", room.getRoomId());
                            roomListActivity.startActivity(intent);
                            return;
                        }
                        Room roomWithNewInfo = JsonUtils.fromJson(usedForHttpMessage.getData().toString(), Room.class);
                        viewHold.roomStateTv.setText(String.format(Locale.CHINA, "人数%d/%d", roomWithNewInfo.getCurrentPlayer(), roomWithNewInfo.getMaxPlayer()));
                        roomListActivity.snackMessage("房间人数已满");
                    }, throwable -> {
                        ToastUtils.makeShortText(throwable.getMessage(), roomListActivity);
                        roomListActivity.hideProgressBar();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return mRoomList.size();
    }

    class MyViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_room_id)
        TextView roomIdTv;
        @BindView(R.id.tv_room_type)
        TextView roomTypeTv;
        @BindView(R.id.tv_room_state)
        TextView roomStateTv;

        public MyViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
