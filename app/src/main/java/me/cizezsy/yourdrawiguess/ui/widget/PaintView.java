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

import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import me.cizezsy.yourdrawiguess.model.PlayerMessage;
import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.net.MyWebSocketClient;
import me.cizezsy.yourdrawiguess.util.JsonUtils;

public class PaintView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final float THICK_WIDTH = 30;
    public static final float NORMAL_WIDTH = 20;
    public static final float THIN_WIDTH = 10;

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean startDraw;

    private Path mNetPath = new Path();
    private Path mNaivePath = new Path();
    private Paint mNetPaint = new Paint();
    private Paint mNaivePaint = new Paint();

    private ConcurrentSkipListMap<Long, PathWithPaint> mTimeAndPathWithPaintMap = new ConcurrentSkipListMap<>();



    private boolean isToMe = true;
    private MyWebSocketClient client;
    private List<Step> steps = new ArrayList<>();

    private volatile LinkedBlockingQueue<Step> mStepQueue = new LinkedBlockingQueue<>();


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
        startSendStepTask();

        mNetPaint.setStyle(Paint.Style.STROKE);
        mNetPaint.setColor(Color.BLACK);
        mNetPaint.setStrokeWidth(20);

        mNaivePaint.setStyle(Paint.Style.STROKE);
        mNaivePaint.setColor(Color.BLACK);
        mNaivePaint.setStrokeWidth(NORMAL_WIDTH);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (!isToMe)
            return true;

        float x = event.getX();
        float y = event.getY();

        Step step = new Step();
        step.setX(x);
        step.setY(y);

        step.setDeviceHeight(getHeight());
        step.setDeviceWidth(getWidth());
        step.setColor(mNaivePaint.getColor());
        step.setTextSize(mNaivePaint.getStrokeWidth());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                step.setType(0);
                mNaivePath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                step.setType(1);
                mNaivePath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                step.setType(2);
                mTimeAndPathWithPaintMap.put(step.getTime(), new PathWithPaint(mNaivePath, mNaivePaint));
                mNaivePath = new Path();
                break;
        }

        mStepQueue.add(step);
        steps.add(step);
        return true;
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            for(Map.Entry<Long, PathWithPaint> entry : mTimeAndPathWithPaintMap.entrySet()) {
                PathWithPaint pathWithPaint = entry.getValue();
                mCanvas.drawPath(pathWithPaint.path, pathWithPaint.paint);
            }
            mCanvas.drawPath(mNetPath, mNetPaint);
            mCanvas.drawPath(mNaivePath, mNaivePaint);

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
        client.setPaintView(this);
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
        step.setX(x);
        step.setY(y);
        step.setDeviceWidth(getWidth());
        step.setDeviceHeight(getHeight());
        steps.add(step);

        if (step.getColor() != mNetPaint.getColor()
                || step.getTextSize() != mNetPaint.getStrokeWidth()) {
            //mTimeAndPathWithPaintMap.put(step.getTime(), new PathWithPaint(mNetPath, mNetPaint));
            Paint tempPaint = new Paint();
            tempPaint.setStyle(mNetPaint.getStyle());
            tempPaint.setStrokeWidth(step.getTextSize());
            tempPaint.setColor(step.getColor());
            mNetPaint = tempPaint;
            //mNetPath = new Path();
        }

        if (step.getType() == 0) {
            mNetPath.moveTo(x, y);
        } else if (step.getType() == 1) {
            mNetPath.lineTo(x, y);
        } else if(step.getType() == 2) {
            mTimeAndPathWithPaintMap.put(System.nanoTime(), new PathWithPaint(mNetPath, mNetPaint));
            mNetPath = new Path();
        }
    }


    public void refreshPath(List<Step> stepList) {
        for (Step s : stepList) {
            refreshPath(s);
        }
    }

    public void setToMe(boolean toMe) {
        isToMe = toMe;
    }


    public void startSendStepTask() {
        new Thread(() -> {
            while (true) {
                try {
                    Step step = mStepQueue.take();
                    PlayerMessage message = new PlayerMessage<>(PlayerMessage.Type.DRAW, step);
                    String json = JsonUtils.toJson(message);
                    try {
                        if(!client.getConnection().isOpen()) {
                            client.connect();
                        }
                        client.send(json);
                    } catch (WebsocketNotConnectedException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void setColor(int color) {
        //PathWithPaint pathWithPaint = new PathWithPaint(mNaivePath, mNaivePaint);
        //mTimeAndPathWithPaintMap.put(System.currentTimeMillis(), pathWithPaint);
        Paint tempPaint = new Paint();
        tempPaint.setColor(color);
        tempPaint.setStrokeWidth(mNaivePaint.getStrokeWidth());
        tempPaint.setStyle(mNaivePaint.getStyle());
        mNaivePaint = tempPaint;
     //   mNaivePath = new Path();
    }

    public void setStrokeWidth(float width) {
        //PathWithPaint pathWithPaint = new PathWithPaint(mNaivePath, mNaivePaint);
       // mTimeAndPathWithPaintMap.put(System.currentTimeMillis(), pathWithPaint);
        Paint tempPaint = new Paint();
        tempPaint.setColor(mNaivePaint.getColor());
        tempPaint.setStrokeWidth(width);
        tempPaint.setStyle(mNaivePaint.getStyle());
        mNaivePaint = tempPaint;
       // mNaivePath = new Path();
    }

    private class PathWithPaint {
        Path path;
        Paint paint;

        public PathWithPaint(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }
}
