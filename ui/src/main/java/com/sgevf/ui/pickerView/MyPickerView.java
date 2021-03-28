package com.sgevf.ui.pickerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sgevf.ui.R;

import java.util.ArrayList;
import java.util.List;

public class MyPickerView extends View implements IPicker {
    //picker 默认宽度
    private static final int DEFAULT_PICKER_WIDTH = 400;
    //picker 默认高度
    private static final int DEFAULT_PICKER_HEIGHT = 600;
    //默认选中颜色
    private static final int DEFAULT_SELECT_COLOR = Color.BLACK;
    //默认未选中颜色
    private static final int DEFAULT_NORMAL_COLOR = Color.parseColor("#333333");
    //默认透明度
    private static final int DEFAULT_TEXT_ALPHA = 255;
    //默认字体大小
    private static final int DEFAULT_TEXT_SIZE = 30;
    //默认行间距系数，最大字体
    private static final float LINE_SPACE_RATIO = 0.4f;
    //picker 宽度
    private int mPickerWidth;
    //picker 高度
    private int mPickerHeight;
    //选中颜色
    private int mSelectColor;
    //未选中颜色
    private int mNormalColor;
    //最大字体大小
    private float mMaxTextSize;
    //最小字体大小
    private float mMinTextSize;
    //最大透明度
    private int mMaxTextAlpha;
    //最小透明度
    private int mMinTextAlpha;
    //选中的画笔
    private Paint mSelectPaint;
    //未选中的画笔
    private Paint mNormalPaint;


    private int mPosition = 0;
    private List<MyPickerView.IPickerData> mData;


    public MyPickerView(Context context) {
        this(context, null);
    }

    public MyPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyPickerView);
        mSelectColor = array.getColor(R.styleable.MyPickerView_selectColor, DEFAULT_SELECT_COLOR);
        mNormalColor = array.getColor(R.styleable.MyPickerView_normalColor, DEFAULT_NORMAL_COLOR);
        mMaxTextSize = array.getDimensionPixelSize(R.styleable.MyPickerView_maxTextSize, DEFAULT_TEXT_SIZE);
        mMinTextSize = array.getDimensionPixelSize(R.styleable.MyPickerView_minTextSize, DEFAULT_TEXT_SIZE);
        mMaxTextAlpha = array.getInt(R.styleable.MyPickerView_maxTextAlpha, DEFAULT_TEXT_ALPHA);
        mMinTextAlpha = array.getInt(R.styleable.MyPickerView_minTextAlpha, DEFAULT_TEXT_ALPHA);
        array.recycle();
    }

    private void initPaint() {
        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setTextSize(mMaxTextSize);
        mSelectPaint.setColor(mSelectColor);
        mSelectPaint.setAlpha(mMaxTextAlpha);
        mSelectPaint.setTextAlign(Paint.Align.CENTER);

        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalPaint.setColor(mNormalColor);
        mNormalPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mPickerWidth = MeasureSpec.getSize(widthMeasureSpec);
        mPickerHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mPickerWidth = DEFAULT_PICKER_WIDTH;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mPickerHeight = DEFAULT_PICKER_HEIGHT;
        }
        setMeasuredDimension(mPickerWidth, mPickerHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSelectData(canvas);
        float y = mPickerHeight / 2.0f - getFontHeight(mSelectPaint) / 2.0f;
        for (int i = 1; (mPosition - i) >= 0; i++) {
            y = drawOtherData(canvas, i, -1, y);
        }
        y = mPickerHeight / 2.0f + getFontHeight(mSelectPaint) / 2.0f;
        for (int i = 1; (mPosition + i) < mData.size(); i++) {
            y = drawOtherData(canvas, i, 1, y);
            Log.d("TAG", "onDraw: " + y);
        }
    }

    /**
     * 绘制选中
     *
     * @param canvas
     */
    private void drawSelectData(Canvas canvas) {
        String data = mData.get(mPosition).showString();
        float x = mPickerWidth / 2.0f;
        float y = mPickerHeight / 2.0f;
        Paint.FontMetrics fm = mSelectPaint.getFontMetrics();
        float baseline = y - (fm.top + fm.bottom) / 2;
        canvas.drawText(data, x, baseline, mSelectPaint);
    }

    /**
     * 绘制未选中
     *
     * @param canvas
     * @param position  距离中间的第几个
     * @param direction 方向 -1 上方，1 下方
     */
    private float drawOtherData(Canvas canvas, int position, int direction, float h) {
        float y = h + LINE_SPACE_RATIO * getFontHeight(mSelectPaint) * direction;
        float scale = function(y);
        mNormalPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize) * scale);
        mNormalPaint.setAlpha((int) (mMinTextAlpha + (mMaxTextAlpha - mMinTextAlpha) * scale));
        float x = mPickerWidth / 2.0f;
        Paint.FontMetrics fm = mNormalPaint.getFontMetrics();
        float baseline = y - (fm.top + fm.bottom) / 2;
        canvas.drawText(mData.get(mPosition + direction * position).showString(), x, baseline, mNormalPaint);
        return y + direction * getFontHeight(mNormalPaint) / 2.0f;
    }

    private float function(float x) {
        float a = 1.0f / mPickerHeight;
        float f = (float) (-4 * a * a * x * x + 4 * a * x);
        return f < 0 ? 0 : f;
    }

    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    @Override
    public <T extends IPickerData> void setData(List<T> list) {
        setData(0, list);
    }

    @Override
    public <T extends IPickerData> void setData(int position, List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        mPosition = (position < 0 || position > list.size() - 1) ? 0 : position;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        for (T t : list) {
            mData.add(t);
        }
        invalidate();
    }

    @Override
    public void setOnPickerSelectListener(OnPickerSelectListener listener) {

    }

    public interface IPickerData {
        //展示的字符串
        String showString();
    }

    public interface OnPickerSelectListener {
        void onSelect(int position, IPickerData data);
    }
}
