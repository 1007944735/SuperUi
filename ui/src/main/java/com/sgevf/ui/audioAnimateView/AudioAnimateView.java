package com.sgevf.ui.audioAnimateView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AudioAnimateView extends View {
    private Paint mPaint;
    private String color = "#ff0000";
    private int columnWidth = 30;
    private int width;
    private int height;
    private int maxHeight = 200;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float m = 0;
    private float n = 0;

    public AudioAnimateView(Context context) {
        this(context, null);
    }

    public AudioAnimateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioAnimateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor(color));
        ValueAnimator animatorX = ValueAnimator.ofFloat(0, 1);
        animatorX.setDuration(1000);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.setRepeatCount(-1);
        animatorX.setRepeatMode(ValueAnimator.REVERSE);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorX.start();

        ValueAnimator animatorY = ValueAnimator.ofFloat(0, 1);
        animatorY.setDuration(1300);
        animatorY.setInterpolator(new LinearInterpolator());
        animatorY.setRepeatCount(-1);
        animatorY.setRepeatMode(ValueAnimator.REVERSE);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                y = (float) animation.getAnimatedValue();
            }
        });
        animatorY.start();

        ValueAnimator animatorZ = ValueAnimator.ofFloat(0, 1);
        animatorZ.setDuration(800);
        animatorZ.setInterpolator(new LinearInterpolator());
        animatorZ.setRepeatCount(-1);
        animatorZ.setRepeatMode(ValueAnimator.REVERSE);
        animatorZ.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                z = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorZ.start();


        ValueAnimator animatorM = ValueAnimator.ofFloat(0, 1);
        animatorM.setDuration(1500);
        animatorM.setInterpolator(new LinearInterpolator());
        animatorM.setRepeatCount(-1);
        animatorM.setRepeatMode(ValueAnimator.REVERSE);
        animatorM.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                m = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorM.start();

        ValueAnimator animatorN = ValueAnimator.ofFloat(0, 1);
        animatorN.setDuration(2000);
        animatorN.setInterpolator(new LinearInterpolator());
        animatorN.setRepeatCount(-1);
        animatorN.setRepeatMode(ValueAnimator.REVERSE);
        animatorN.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                n = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorN.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = getPaddingLeft() + columnWidth * 5 + getPaddingRight();
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + maxHeight + getPaddingBottom();
        } else {
            height = heightSize;
        }
        columnWidth = (width - getPaddingLeft() - getPaddingRight()) / 5;
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int start = paddingLeft;
        for (int i = 0; i < 5; i++) {
            canvas.drawRect(start + i * columnWidth, paddingTop + getTop(i), start + (i + 1) * columnWidth, paddingTop + maxHeight, mPaint);
        }
    }

    private int getTop(int index) {

        switch (index) {
            case 0:
                return (int) (x * maxHeight / 2 + maxHeight / 2);
            case 1:
                return (int) (y * maxHeight);
            case 2:
                return (int) (z * (maxHeight / 2));
            case 3:
                return (int) (m * (maxHeight * 3 / 4));
            case 4:
                return (int) (n * (maxHeight * 2 / 3));
        }
        return 0;
    }

    ;


}
