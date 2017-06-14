package me.cizezsy.yourdrawiguess.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.model.Phrase;
import me.cizezsy.yourdrawiguess.ui.activity.GameActivity;

public class PhraseSelectFragment extends AppCompatDialogFragment {

    private List<Phrase> mPhraseList;
    private int mTime = 12;

    @BindView(R.id.tv_phrase_time_alert)
    TextView mTimeCountTv;
    @BindView(R.id.rb_phrase1)
    AppCompatRadioButton mPhraseRb1;
    @BindView(R.id.rb_phrase2)
    AppCompatRadioButton mPhraseRb2;
    @BindView(R.id.rb_phrase3)
    AppCompatRadioButton mPhraseRb3;
    @BindView(R.id.rb_phrase4)
    AppCompatRadioButton mPhraseRb4;
    private Activity mActivity;
    private TimerTask timeCountTask;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_phrase_select, null);
        ButterKnife.bind(this, view);
        init();
        return new AlertDialog.Builder(getContext())
                .setTitle("请选择一个词语")
                .setPositiveButton("确定", (dialog, which) -> {
                    if (mPhraseList.size() < 4) return;
                    Phrase currentPhrase;
                    if (mPhraseRb2.isChecked()) {
                        currentPhrase = mPhraseList.get(1);
                    } else if (mPhraseRb3.isChecked()) {
                        currentPhrase = mPhraseList.get(2);
                    } else if (mPhraseRb4.isChecked()) {
                        currentPhrase = mPhraseList.get(3);
                    } else {
                        currentPhrase = mPhraseList.get(0);
                    }
                    ((GameActivity) getActivity()).selectPhrase(currentPhrase);
                    getDialog().dismiss();
                })
                .setView(view)
                .setCancelable(false)
                .create();
    }

    private void init() {
        timeCountTask = new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(() -> mTimeCountTv.setText(String.format(Locale.CHINA, "还有%d秒", mTime--)));
                if (mTime == 0) {
                    getDialog().dismiss();
                    this.cancel();
                }
            }
        };
        new Timer().schedule(timeCountTask, 0, 1000);
        mPhraseRb1.setText(mPhraseList.get(0).getName());
        mPhraseRb2.setText(mPhraseList.get(1).getName());
        mPhraseRb3.setText(mPhraseList.get(2).getName());
        mPhraseRb4.setText(mPhraseList.get(3).getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timeCountTask.cancel();
    }

    public static PhraseSelectFragment newInstance(List<Phrase> phraseList, Activity activity) {
        PhraseSelectFragment fragment = new PhraseSelectFragment();
        fragment.mPhraseList = phraseList;
        fragment.mActivity = activity;
        return fragment;
    }

}
