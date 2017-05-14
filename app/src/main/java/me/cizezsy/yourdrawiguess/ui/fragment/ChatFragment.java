package me.cizezsy.yourdrawiguess.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.adapter.ChatRecyclerViewAdapter;
import me.cizezsy.yourdrawiguess.model.Chat;
import me.cizezsy.yourdrawiguess.ui.activity.GameActivity;

public class ChatFragment extends Fragment {

    @BindView(R.id.rv_chat)
    RecyclerView mChatRv;

    @BindView(R.id.tv_now_not_have_new_mes)
    TextView notHaveMesTv;
    private List<Chat> mChatList;
    private ChatRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Activity activity = getActivity();
        if (activity instanceof GameActivity) {
            mChatList = ((GameActivity) activity).getChatList();
        } else {
            mChatList = new ArrayList<>();
        }
        mAdapter = new ChatRecyclerViewAdapter(mChatList, activity);
        mChatRv.setAdapter(mAdapter);
        mChatRv.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void notifyChatAdd() {
        if (notHaveMesTv.getVisibility() == View.VISIBLE) {
            notHaveMesTv.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }
}
