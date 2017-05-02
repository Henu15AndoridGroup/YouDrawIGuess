package me.cizezsy.yourdrawiguess.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;
import me.cizezsy.yourdrawiguess.util.JsonUtils;
import rx.Observable;
import rx.Subscriber;

//绘图view
public class PaintView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private boolean startDraw;
    private Path mPath = new Path();

    private boolean isToMe = true;

    private MyWebSocketClient client;
    private List<Step> steps = new ArrayList<>();

    private float x;
    private float y;


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(!isToMe)
            return true;

        x = event.getX();
        y = event.getY();

        Step step = new Step();
        step.setX(x);
        step.setY(y);

        step.setDeviceHeight(getHeight());
        step.setDeviceWidth(getWidth());
        step.setColor(mPaint.getColor());
        step.setTextSize(mPaint.getTextSize());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                step.setType(0);
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:

                step.setType(1);
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        new Thread(() -> client.send(JsonUtils.toJson(client)));

        steps.add(step);
        return true;
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.STROKE);

            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(20);
            mCanvas.drawPath(mPath, mPaint);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void setClient(MyWebSocketClient client) {
        this.client = client;

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startDraw = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        startDraw = false;
    }

    @Override
    public void run() {
        while (startDraw) {
            draw();
        }
    }

    public void refreshPath(Step step) {
        float x = (getWidth() / step.getDeviceWidth()) * step.getX();
        float y = (getHeight() / step.getDeviceHeight()) * step.getY();

        if(step.getType() == 0) {
            mPath.moveTo(x, y);
        } else if(step.getType() == 1) {
            mPath.lineTo(x, y);
        }
    }

    public void setToMe(boolean toMe) {
        isToMe = toMe;
    }
}
