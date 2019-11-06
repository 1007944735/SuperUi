package com.sgevf.ui.pickerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sgevf.ui.R;
import com.sgevf.ui.utils.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PickerView extends View {
    //
    public static final float MARGIN_ALPHA = 2.8f;
    public static final float SPEED = 10;
    private Context context;
    //宽度
    private int pickerWidth;
    //高度
    private int pickerHeight;
    //最大字体
    private float maxTextSize;
    //最小字体
    private float minTextSize;
    //最大透明度
    private float maxTextAlpha = 255;
    //最小透明度
    private float minTextAlpha = 120;

    private Paint curPaint;
    private Paint othPaint;

    private boolean isInit = false;

    private float moveLen = 0;
    private float lastY;

    private List<String> dataList;
    private List<String> sourceList;
    private int currentItem;
    private int selectItem;

    private boolean loop = true;

    private Timer timer;
    private PickerTimerTask pTimerTask;

    private OnSelectListener selectListener;

    private int selectColor;
    private int otherColor;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(moveLen) < SPEED) {
                moveLen = 0;
                if (pTimerTask != null) {
                    pTimerTask.cancel();
                    pTimerTask = null;
                    performSelect();
                }
            } else {
                moveLen = moveLen - moveLen / Math.abs(moveLen) * SPEED;
            }
            invalidate();
        }
    };


    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
        selectColor = t.getColor(R.styleable.PickerView_selectColor, Color.RED);
        otherColor = t.getColor(R.styleable.PickerView_otherColor, Color.parseColor("#333333"));
        t.recycle();
        init();
    }

    private void init() {
        timer = new Timer();
        dataList = new ArrayList<>();
        sourceList = new ArrayList<>();
        curPaint = new Paint();
        curPaint.setAntiAlias(true);
        curPaint.setStyle(Paint.Style.FILL);
        curPaint.setTextAlign(Paint.Align.CENTER);
        curPaint.setColor(selectColor);
        othPaint = new Paint();
        othPaint.setAntiAlias(true);
        othPaint.setStyle(Paint.Style.FILL);
        othPaint.setTextAlign(Paint.Align.CENTER);
        othPaint.setColor(otherColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pickerWidth = getMeasuredWidth();
        pickerHeight = getMeasuredHeight();

        maxTextSize = pickerHeight / 7f;
        minTextSize = maxTextSize / 2;

        isInit = true;

        invalidate();
    }

    public void setData(List<String> list) {
        this.dataList = list;
        ParseUtil.copyList(list, sourceList);
        currentItem = list.size() / 4;
        selectItem = currentItem;
        invalidate();
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit && !dataList.isEmpty()) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        //绘制中心选中的data
        float scale = function(pickerHeight / 4.0f, moveLen);
        float size = minTextSize + (maxTextSize - minTextSize) * scale;
        curPaint.setTextSize(size);
        curPaint.setAlpha((int) (minTextAlpha + (maxTextAlpha - minTextAlpha) * scale));
        float x = pickerWidth / 2.0f;
        float y = pickerHeight / 2.0f + moveLen;
        Paint.FontMetricsInt fm = curPaint.getFontMetricsInt();
        float baseline = (float) (y - (fm.top / 2.0 + fm.bottom / 2.0));
        canvas.drawText(dataList.get(currentItem), x, baseline, curPaint);
        //绘制上方
        for (int i = 1; (currentItem - i) >= 0; i++) {
            drawOtherData(canvas, i, -1);
        }
        //绘制下方
        for (int i = 1; (currentItem + i) < dataList.size(); i++) {
            drawOtherData(canvas, i, 1);
        }
    }

    /**
     * 绘制其他的Text
     *
     * @param canvas
     * @param position
     * @param type
     */
    private void drawOtherData(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * minTextSize * position + type * moveLen;
        float scale = function(pickerHeight / 4.0f, d);
        float size = minTextSize + (maxTextSize - minTextSize) * scale;
        othPaint.setTextSize(size);
        othPaint.setAlpha((int) (minTextAlpha + (maxTextAlpha - minTextAlpha) * scale));
        float x = pickerWidth / 2.0f;
        float y = pickerHeight / 2.0f + d * type;
        Paint.FontMetricsInt fm = curPaint.getFontMetricsInt();
        float baseline = (float) (y - (fm.top / 2.0 + fm.bottom / 2.0));
        canvas.drawText(dataList.get(currentItem + type * position), x, baseline, othPaint);
    }

    private float function(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pTimerTask != null) {
                    pTimerTask.cancel();
                    pTimerTask = null;
                }
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveLen += (event.getY() - lastY);
                if (moveLen > MARGIN_ALPHA * minTextSize / 2) {
                    if (!loop && currentItem == 0) {
                        lastY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        currentItem--;
                        selectItem--;
                    } else {
                        selectItem--;
                        if (selectItem < 0) {
                            selectItem = selectItem + dataList.size();
                        }
                    }

                    moveTailToHead();
                    moveLen = moveLen - MARGIN_ALPHA * minTextSize;
                } else if (moveLen < -MARGIN_ALPHA * minTextSize / 2) {
                    if (currentItem == dataList.size() - 1) {
                        lastY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) {
                        currentItem++;
                        selectItem++;
                    } else {
                        selectItem++;
                        selectItem = selectItem % dataList.size();
                    }
                    moveHeadToTail();
                    moveLen = moveLen + MARGIN_ALPHA * minTextSize;
                }
                lastY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doUp(MotionEvent event) {
        if (Math.abs(moveLen) < 0.0001) {
            moveLen = 0;
            return;
        }
        if (pTimerTask != null) {
            pTimerTask.cancel();
            pTimerTask = null;
        }

        pTimerTask = new PickerTimerTask(handler);
        timer.schedule(pTimerTask, 0, 10);
    }

    private void moveHeadToTail() {
        if (loop) {
            String head = dataList.get(0);
            dataList.remove(0);
            dataList.add(head);
        }
    }

    private void moveTailToHead() {
        if (loop) {
            String tail = dataList.get(dataList.size() - 1);
            dataList.remove(dataList.size() - 1);
            dataList.add(0, tail);
        }
    }

    private void performSelect() {
        if (selectListener != null) {
            selectListener.onSelect(sourceList.get(selectItem));
        }
    }

    public String getSelect() {
        return sourceList.get(selectItem);
    }

    /**
     * 选择选中的内容
     *
     * @param selected
     */
    public void setSelectItem(String selected) {
        for (int i = 0; i < sourceList.size(); i++) {
            if (selected.equals(sourceList.get(i))) {
                setSelectItem(i);
                break;
            }
        }
    }

    /**
     * 选择选中的内容
     *
     * @param selected
     */
    public void setSelectItem(int selected) {
        if (selected >= 0 && selected < dataList.size()) {
            String result = sourceList.get(selected);
            for (int i = 0; i < dataList.size(); i++) {
                if (result.equals(dataList.get(i))) {
                    currentItem = i;
                    break;
                }
            }
            selectItem = selected;
        } else {
            return;
        }
        if (loop) {
            int distance = dataList.size() / 2 - currentItem;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    currentItem--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    currentItem++;
                }
            }
        }
        invalidate();
    }

    public void selectLast() {
        selectItem--;
        selectItem = selectItem < 0 ? selectItem + dataList.size() : selectItem;
        setSelectItem(selectItem);
    }

    public void selectNext() {
        selectItem++;
        selectItem = selectItem % dataList.size();
        setSelectItem(selectItem);
    }

    public interface OnSelectListener {
        void onSelect(String text);
    }

    public void setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    class PickerTimerTask extends TimerTask {
        Handler handler;

        public PickerTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
}
