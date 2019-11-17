package com.sgevf.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollStarView extends View {
    private int width;
    private int height;
    private Paint textPaint;
    private Paint starPaint;
    private final int textColor = Color.parseColor("#333333");
    private final float textSize = 40.0f;
    private final float speed = 5;
    private final int LEFT = -1;
    private final int RIGHT = 1;

    private List<ScrollStarInfoBean> stars;
    private int starNum;
    private int currentPos;
    private boolean init;
    private float lastX;
    private float moveX = 0;
    private float a;//抛物线的系数,类 y=a*x*x;

    private Timer timer;
    private ScrollTimeTask timeTask;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(moveX) < speed) {
                moveX = 0;
                if (timeTask != null) {
                    timeTask.cancel();
                    timeTask = null;
                }
            } else {
                moveX = moveX - moveX / Math.abs(moveX) * speed;
                Log.d("TAG", "handleMessage: "+moveX);
            }
            invalidate();
        }
    };

    public ScrollStarView(Context context) {
        this(context, null);
    }

    public ScrollStarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        a = (height * 0.5f) / ((width / 2.0f) * (width / 2.0f));
    }

    public void addStar(List<Integer> starImages) {
        if (starImages == null) return;
        this.stars.clear();
        for (int i = 0; i < starImages.size(); i++) {
            this.stars.add(new ScrollStarInfoBean(i + 1, starImages.get(i)));
        }
        starNum = starImages.size();
        invalidate();
    }


    private void init() {
        timer = new Timer();
        stars = new ArrayList<>();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (init && !stars.isEmpty()) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        if (starNum % 2 == 0) {
            //偶数
        } else {
            //奇数
            currentPos = starNum / 2;
            float x = width / 2.0f + moveX;
            float y = height * 0.85f;
            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            float baseline = (float) (y - (fm.top / 2.0 + fm.bottom / 2.0));
            canvas.drawText(stars.get(currentPos).num, x, baseline, textPaint);
            Bitmap bp = scaleBitmap(stars.get(currentPos).starId, scaleFunction(moveX));
            float left = width / 2.0f + moveX - bp.getWidth() / 2.0f;
            float top = height / 2.0f + routeFunction(moveX) - bp.getHeight() / 2.0f;
            canvas.drawBitmap(bp, left, top, starPaint);
            //绘制左边
            for (int i = 1; currentPos - i >= 0; i++) {
                drawOther(canvas, i, LEFT);
            }
            //绘制右边
            for (int i = 1; (currentPos + i) < starNum; i++) {
                drawOther(canvas, i, RIGHT);
            }
        }
    }

    /**
     * 绘制左边和右边
     *
     * @param canvas
     * @param i      相对当前位置的偏移
     * @param type   左边 -1，右边 1
     */
    private void drawOther(Canvas canvas, int i, int type) {
        float widthItem = width / starNum;
        float widthHalfItem = widthItem / 2;
        float d = widthItem * i + type * moveX;
        float x = width / 2.0f + type * d;
        float y = height * 0.85f;
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        float baseline = (float) (y - (fm.top / 2.0 + fm.bottom / 2.0));
        canvas.drawText(stars.get(currentPos + type * i).num, x, baseline, textPaint);

        Bitmap bp = scaleBitmap(stars.get(currentPos).starId, scaleFunction(d * type));
        float left = width / 2.0f + type * d - bp.getWidth() / 2.0f;
        float top = height / 2.0f - routeFunction(type * d) - bp.getHeight() / 2.0f;
        canvas.drawBitmap(bp, left, top, starPaint);
    }

    private Bitmap scaleBitmap(int starId, float ratio) {
        Bitmap origin = BitmapFactory.decodeResource(getResources(), starId);
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    //路径函数
    public float routeFunction(float x) {
        return a * x * x;
    }

    //缩放函数
    public float scaleFunction(float x) {
        float widthItem = width / starNum;
        float y = -Math.abs(x) / (5 * widthItem) + 0.5f;
        return y > 0.5f ? 0.5f : y < 0.3f ? 0.3f : y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (timeTask != null) {
                    timeTask.cancel();
                    timeTask = null;
                }
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += event.getX() - lastX;
                float widthItem = width / starNum;
                float widthHalfItem = widthItem / 2;
                if (moveX > widthHalfItem) {
                    currentPos--;
                    moveRightToLeft();
                    moveX = moveX - widthItem;
                } else if (moveX < -widthHalfItem) {
                    currentPos++;
                    moveLeftToRight();
                    moveX = moveX + widthItem;
                }
                lastX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doUp(MotionEvent event) {
        if (Math.abs(moveX) < 0.0001) {
            moveX = 0;
            return;
        }

        if (timeTask != null) {
            timeTask.cancel();
            timeTask = null;
        }

        timeTask = new ScrollTimeTask(handler);
        timer.schedule(timeTask, 0, 10);
    }

    private void moveLeftToRight() {
        ScrollStarInfoBean bean = stars.get(0);
        stars.remove(bean);
        stars.add(bean);
    }

    private void moveRightToLeft() {
        ScrollStarInfoBean bean = stars.get(starNum - 1);
        stars.remove(bean);
        stars.add(0, bean);
    }

    private class ScrollTimeTask extends TimerTask {
        Handler handler;

        ScrollTimeTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    private class ScrollStarInfoBean {
        private int starId;
        private String num;

        ScrollStarInfoBean(int num, int starId) {
            this.num = transformText(num);
            this.starId = starId;
        }

        /**
         * 变化文本，如 3-->03，10-->10
         *
         * @param i
         * @return
         */
        private String transformText(int i) {
            if (0 <= i && i <= 9) {
                return "0" + i;
            } else {
                return i + "";
            }
        }
    }
}
