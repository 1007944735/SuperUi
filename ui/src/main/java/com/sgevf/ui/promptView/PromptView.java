package com.sgevf.ui.promptView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sgevf.ui.R;

public class PromptView extends ViewGroup {
    private int mTipColor;
    private int mTipTextColor;
    private int mTipTextSize;
    private int mTipSize;
    private int childWidth;
    private int childHeight;

    private Paint mCirclePaint;
    private Paint mTextPaint;

    private int content;

    public PromptView(Context context) {
        this(context, null);
    }

    public PromptView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mTipColor);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTipTextSize);
        mTextPaint.setColor(mTipTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.PromptView);
            mTipColor = array.getColor(R.styleable.PromptView_tipColor, Color.parseColor("#FF6805"));
            mTipTextColor = array.getColor(R.styleable.PromptView_tipContentColor, Color.WHITE);
            mTipTextSize = array.getDimensionPixelOffset(R.styleable.PromptView_tipContentSize, 10);
            mTipSize = array.getDimensionPixelOffset(R.styleable.PromptView_tipSize, 18);
            content = array.getInt(R.styleable.PromptView_text, 0);
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0)
            return;
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        childWidth = child.getMeasuredWidth();
        childHeight = child.getMeasuredHeight();
        setMeasuredDimension(childWidth + mTipSize / 2, childHeight + mTipSize / 2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0)
            return;
        View child = getChildAt(0);
        child.layout(0, mTipSize / 2, childWidth, childHeight + mTipSize / 2);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() == 0)
            return;
        super.dispatchDraw(canvas);
        if (content != 0) {
            canvas.drawCircle(childWidth, mTipSize / 2, mTipSize / 2, mCirclePaint);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float baseline = mTipSize / 2 - (fm.top / 2 + fm.bottom / 2);
            canvas.drawText(this.content + "", childWidth, baseline, mTextPaint);
        }
    }

    public void setText(int text) {
        if (text != content) {
            this.content = text;
            invalidate();
        }
    }
}
