package com.sgevf.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private View header;
    private int headerHeight;
    private int curState = 1;//当前header状态
    private final int PULL = 1;//下拉状态
    private final int RELEASE = 2;//提示刷新
    private final int FLASHING = 3;//刷新状态
    private int startY;
    private int firstVisibleItem;//第一个可见item
    private boolean flashEnable;//是否可以刷新

    private ImageView imgArrow;
    private ProgressBar progressBar;
    private TextView tip;

    private View footer;

    private RefreshListener listener;

    private int footerHeight;
    private boolean showHeader=false;
    private int middleY = 0;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        header = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null);
        footer = LayoutInflater.from(context).inflate(R.layout.layout_refresh_footer, null);

        imgArrow = header.findViewById(R.id.imgArrow);
        progressBar = header.findViewById(R.id.progress);
        tip = header.findViewById(R.id.tip);

//        notifyView(header);
//        notifyView(footer);
        header.measure(0, 0);
        footer.measure(0,0);

        headerHeight = header.getMeasuredHeight();
        footerHeight = footer.getMeasuredHeight();

        paddingHeaderTop(-headerHeight);
        paddingFooterTop(-footerHeight);
        this.addHeaderView(header);
        this.addFooterView(footer);

        this.setOnScrollListener(this);
    }

    private void notifyView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;

        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(width, height);
    }

    private void paddingHeaderTop(int pt) {
        header.setPadding(header.getPaddingLeft(), pt, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    private void paddingFooterTop(int pt) {
        footer.setPadding(footer.getPaddingLeft(), pt, footer.getPaddingRight(), footer.getPaddingBottom());
        footer.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() == (getCount() - 1)) {
            paddingFooterTop(0);
            setSelection(getCount());
            if (listener != null) {
                listener.footerRefresh();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int itemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
//                Log.d("TAG", "startY: "+startY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (curState == FLASHING) {
                    break;
                }
                int tempY = (int) ev.getY();
                int space = tempY - startY;//移动距离
                int top = -headerHeight + space;
                Log.d("TAG", "onTouchEvent tempY: "+top);
                if (space > 0 && getFirstVisiblePosition() == 0) {

                    if(!showHeader){
                        middleY= (int) ev.getY();
                        showHeader=true;
                    }
                    int headerTop=tempY-middleY-headerHeight;
                    paddingHeaderTop(headerTop);
                    if (headerTop>=0 && curState == PULL) {
                        curState = RELEASE;
                        refreshHeaderByState();
                    } else if (headerTop<0&& curState == RELEASE) {
                        curState = PULL;
                        refreshHeaderByState();
                    }
                    return true;
                }else {
                    showHeader=false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (curState == RELEASE) {
                    curState = FLASHING;
//                    paddingHeaderTop(0);
                    startAnimation(header.getPaddingTop(),0,listener);
                    refreshHeaderByState();
                } else if (curState == PULL) {
//                    paddingHeaderTop(-headerHeight);
                    startAnimation(header.getPaddingTop(),-headerHeight);
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    public boolean isTop() {
        if (getFirstVisiblePosition() == 0) {
            View firstVisibleItemView = getChildAt(0);
            int top = firstVisibleItemView.getTop();
            if (firstVisibleItemView != null && top >= -2 && top <= 0) {
                return true;
            }
        }
        return false;
    }

    private void startAnimation(int start,int end){
        startAnimation(start,end,null);
    }
    private void startAnimation(int start, int end, final RefreshListener listener){
        ValueAnimator animator=ValueAnimator.ofInt(start,end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i= (int) animation.getAnimatedValue();
                paddingHeaderTop(i);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(listener!=null){
                    listener.headerRefresh();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(200);
        animator.start();
    }

    private void refreshHeaderByState() {
        switch (curState) {
            case PULL:
                progressBar.setVisibility(GONE);
                imgArrow.setVisibility(VISIBLE);
                imgArrow.setImageResource(R.drawable.icon_header_down);
                tip.setText("下拉刷新");
                break;
            case RELEASE:
                progressBar.setVisibility(GONE);
                imgArrow.setVisibility(VISIBLE);
                imgArrow.setImageResource(R.drawable.icon_header_up);
                tip.setText("松开刷新");
                break;
            case FLASHING:
                progressBar.setVisibility(VISIBLE);
                imgArrow.setVisibility(GONE);
                tip.setText("正在刷新");
                break;
        }
    }
    //header刷新结束
    public void eaderRefreshFinish() {
        curState = PULL;
        refreshHeaderByState();
//        paddingHeaderTop(-headerHeight);
        startAnimation(0,-headerHeight);
    }

    //footer刷新结束
    public void footerRefreshFinish() {
        paddingFooterTop(-footerHeight);
    }



    public interface RefreshListener {
        void headerRefresh();

        void footerRefresh();
    }

    public void setOnRefreshListener(RefreshListener listener) {
        this.listener = listener;
    }
}
