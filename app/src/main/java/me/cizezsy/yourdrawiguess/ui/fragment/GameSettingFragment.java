package me.cizezsy.yourdrawiguess.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.cizezsy.yourdrawiguess.R;
import me.cizezsy.yourdrawiguess.ui.activity.GameActivity;
import me.cizezsy.yourdrawiguess.ui.widget.PaintView;

public class GameSettingFragment extends Fragment {

    @BindView(R.id.rb_paint)
    RadioButton mPaintRb;
    @BindView(R.id.rb_eraser)
    RadioButton mEraserRb;
    @BindView(R.id.rb_red)
    RadioButton mRedRb;
    @BindView(R.id.rb_yellow)
    RadioButton mYellowRb;

    @BindView(R.id.rb_normal_width)
    RadioButton mNormalWidthRb;
    @BindView(R.id.rb_thick_width)
    RadioButton mThickWidthRb;
    @BindView(R.id.rb_thin_width)
    RadioButton mThinWidthRb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_setting, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mPaintRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintColor(Color.BLACK);
        });

        mEraserRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintColor(Color.WHITE);
        });
        mRedRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintColor(Color.RED);
        });
        mYellowRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintColor(Color.YELLOW);
        });
        mNormalWidthRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintStrokeWidth(PaintView.NORMAL_WIDTH);
        });
        mThickWidthRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintStrokeWidth(PaintView.THICK_WIDTH);
        });
        mThinWidthRb.setOnClickListener(v -> {
            ((GameActivity) getActivity()).setPaintStrokeWidth(PaintView.THIN_WIDTH);
        });
    }
}
