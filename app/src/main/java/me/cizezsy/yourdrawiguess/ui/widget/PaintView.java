package me.cizezsy.yourdrawiguess.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;

//绘图view
public class PaintView extends SurfaceView {

    private Canvas canvas;
    private Paint paint;
    private MyWebSocketClient client;
    private List<Step> steps = new ArrayList<>();
    private float startX;
    private float startY;

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //TODO 设置画笔
    private void init() {
        paint = new Paint();
        canvas = new Canvas();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float endX = event.getX();
                float endY = event.getY();

                //TODO 绘图

                Step step = new Step();
                step.setStartX(startX);
                step.setStartY(startY);
                step.setEndX(endX);
                step.setEndY(endY);
                step.setDeviceHeight(getHeight());
                step.setDeviceWidth(getWidth());
                step.setColor(paint.getColor());
                step.setTextSize(paint.getTextSize());

                steps.add(step);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setClient(MyWebSocketClient client) {
        this.client = client;
    }
}
