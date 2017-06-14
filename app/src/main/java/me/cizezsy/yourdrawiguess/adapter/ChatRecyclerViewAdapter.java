package me.cizezsy.yourdrawiguess.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Chat> mChatList;

    public ChatRecyclerViewAdapter(List<Chat> chatList, Context context) {
        mContext = context;
        mChatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        Chat chat = mChatList.get(i);
        if (chat.getType() == Chat.Type.SYSTEM) {
            viewHolder.mChatPlayerNameTv.setTextColor(Color.RED);
            viewHolder.mChatContentTv.setTextColor(Color.RED);
        } else {
            viewHolder.mChatPlayerNameTv.setTextColor(Color.BLACK);
            viewHolder.mChatContentTv.setTextColor(Color.BLACK);
        }
        viewHolder.mChatPlayerNameTv.setText(chat.getUsername());
        viewHolder.mChatContentTv.setText(chat.getContent());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_chat_player_name)
        TextView mChatPlayerNameTv;
        @BindView(R.id.tv_chat_content)
        TextView mChatContentTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
