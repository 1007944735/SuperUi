package com.sgevf.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sgevf.ui.R;
import com.sgevf.ui.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class FlowHorizontalView extends RelativeLayout {
    private int mRowSpace;//行距
    private int mViewSpace;//view间距
    private float width;
    private float height;

    private List<RowInfo> sortList;
    private View selected;

    private FlowHorizontalAdapter mAdapter;
    private OnClickItemListener mListener;

    public FlowHorizontalView(Context context) {
        this(context, null);
    }

    public FlowHorizontalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FlowHorizontalView);
        try {
            mRowSpace = array.getDimensionPixelSize(R.styleable.FlowHorizontalView_rowSpace, DensityUtil.dip2px(getContext(), 10));
            mViewSpace = array.getDimensionPixelSize(R.styleable.FlowHorizontalView_viewSpace, DensityUtil.dip2px(getContext(), 10));
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sortList = new ArrayList<>();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = 0;
        int surplusWidth = widthSize;
        if (mAdapter != null) {
            int childNum = mAdapter.getCount();
            if (childNum < 0) return;
            RowInfo rowInfo = null;
            for (int i = 0; i < childNum; i++) {
                if (surplusWidth == widthSize) {
                    rowInfo = new RowInfo(0, new ArrayList<View>());
                }
                View child = getChildAt(i);
                if (child != null) {
                    child.measure(0, 0);
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    if (rowInfo.getView().isEmpty() && surplusWidth > childWidth) {
                        rowInfo.maxHeight = Math.max(rowInfo.maxHeight, childHeight);
                        rowInfo.getView().add(child);
                        surplusWidth = surplusWidth - childWidth;
                    } else if (surplusWidth > mViewSpace + childWidth) {
                        rowInfo.maxHeight = Math.max(rowInfo.maxHeight, childHeight);
                        rowInfo.getView().add(child);
                        surplusWidth = surplusWidth - mViewSpace - childWidth;
                    } else {
                        //换行
                        surplusWidth = widthSize;
                        if (sortList.isEmpty()) {
                            heightSize = heightSize + rowInfo.maxHeight;
                        } else {
                            heightSize = heightSize + rowInfo.maxHeight + mRowSpace;
                        }
                        sortList.add(rowInfo);
                    }
                }
                if (i == childNum - 1) {
                    if (sortList.isEmpty()) {
                        heightSize = heightSize + rowInfo.maxHeight;
                    } else {
                        heightSize = heightSize + rowInfo.maxHeight + mRowSpace;
                    }
                    sortList.add(rowInfo);
                }
            }
            setMeasuredDimension(widthSize, heightSize);
        } else {
            setMeasuredDimension(widthSize, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < sortList.size(); i++) {
            int left = 0;
            RowInfo info = sortList.get(i);
            for (int j = 0; j < info.getView().size(); j++) {
                View child = info.getView().get(j);
                child.layout(left, top, left + child.getMeasuredWidth(), top + info.maxHeight);
                left = left + mViewSpace + +child.getMeasuredWidth();
            }
            top = top + info.maxHeight + mRowSpace;
        }
    }

    public void setAdapter(FlowHorizontalAdapter adapter) {
        mAdapter = adapter;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getItemView();
            addOnClickListener(item, i);
            addView(item);
            adapter.loadData(item, adapter.getDatas().get(i), i);
        }
        requestLayout();
    }

    private void addOnClickListener(View item, final int position) {
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    return;
                }
                if (selected != null) {
                    selected.setSelected(false);
                }
                v.setSelected(true);
                selected = v;
                if (mListener != null) {
                    mListener.onSelected(position);
                }
            }
        });
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.mListener = listener;
    }

    public interface OnClickItemListener {
        void onSelected(int position);
    }

    public void setSelected(int position) {
        if (position < 0 || position > mAdapter.getCount() - 1) {
            return;
        }
        if (selected != null) {
            selected.setSelected(false);
        }
        getChildAt(position).setSelected(true);
        selected = getChildAt(position);
    }

    class RowInfo {
        int maxHeight;
        List<View> view;

        public RowInfo(int maxHeight, List<View> view) {
            this.maxHeight = maxHeight;
            this.view = view;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public List<View> getView() {
            return view;
        }

        public void setView(List<View> view) {
            this.view = view;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }
    }

    public static abstract class FlowHorizontalAdapter<T> {
        private Context mContext;
        private List<T> mDatas;

        public FlowHorizontalAdapter(Context context, List<T> datas) {
            mContext = context;
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            mDatas.clear();
            mDatas.addAll(datas);
        }

        public abstract int getCount();

        public abstract View getItemView();

        public abstract void loadData(View item, T data, int postion);

        protected List<T> getDatas() {
            return mDatas;
        }

        protected Context getContext() {
            return mContext;
        }
    }
}
