package com.sgevf.ui.panDisplayView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sgevf.ui.R;

public class PanDisplayView extends View {
    private Paint mPaint;
    private int contentRes;
    private Bitmap contentBitmap;
    private int contentWidth;
    private int contentHeight;
    private BitmapShader bs;
    private Matrix matrix = new Matrix();

    private ValueAnimator animator;
    private float offset;

    public PanDisplayView(Context context) {
        this(context, null);
    }

    public PanDisplayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PanDisplayView);
        try {
            contentRes = array.getResourceId(R.styleable.PanDisplayView_contentRes, 0);
        } finally {
            array.recycle();
        }
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentBitmap = getContentBitmap(contentRes);
        animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private Bitmap getContentBitmap(int contentRes) {
        if (contentRes == 0) {
            return null;
        }
        Bitmap bp = BitmapFactory.decodeResource(getResources(), contentRes);
        if (bp != null) {
            contentWidth = bp.getWidth();
            contentHeight = bp.getHeight();
            bs = new BitmapShader(bp, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        }
        return bp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.EXACTLY));
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.EXACTLY));
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float scaleX = 0.0f;
        float scaleY = 0.0f;
        if (contentWidth >= width) {
            scaleX = width * 1.0f / contentWidth;
        }
        if (contentHeight >= height) {
            scaleY = height * 1.0f / contentHeight;
        }
        matrix.setScale(Math.max(scaleX, scaleY), Math.max(scaleX, scaleY));
        bs.setLocalMatrix(matrix);
        mPaint.setShader(bs);
        canvas.drawRect(0, 0, offset * width, height, mPaint);
    }

    public void start() {
        if (animator != null) {
            animator.start();
        }
    }

    public void addAnimatorListener(Animator.AnimatorListener listener) {
        if (animator != null) {
            animator.addListener(listener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.end();
            animator = null;
        }
    }
}
