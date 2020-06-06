package com.sgevf.ui.headBubbleView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sgevf.ui.utils.DensityUtil;

import java.util.Random;

public class HeadBubbleView extends FrameLayout {
    private int parentWidth = DensityUtil.dip2px(getContext(), 60);
    private int parentHeight = DensityUtil.dip2px(getContext(), 130);
    private int circleWidth = DensityUtil.dip2px(getContext(), 22);
    private int circleHeight = DensityUtil.dip2px(getContext(), 22);

    private ImageView fixImageView;

    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.YELLOW, Color.CYAN};
    private int position = 0;

    private Point controlFirstPoint = new Point();
    private Point controlSecondPoint = new Point();
    private Point finishPoint = new Point((int) (new Random().nextFloat() * parentWidth), (int) (circleHeight * 0.5f));

    private float[] pos = new float[2];

    private Handler handler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            createAnimView();
            handler.postDelayed(this, 1800);
        }
    };

    public HeadBubbleView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HeadBubbleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeadBubbleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY));
    }

    private void initView() {
        fixImageView = getImageView();
        initData(fixImageView);
        handler.postDelayed(mRunnable, 1000);
    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(circleWidth, circleHeight);
        imageView.setTranslationX(parentWidth * 0.5f - circleWidth * 0.5f);
        imageView.setTranslationY(parentHeight - circleHeight);
        addView(imageView, lp);
        return imageView;
    }

    private void initData(ImageView imageView) {
        imageView.setBackgroundColor(colors[checkRightPosition(position)]);
    }

    private int checkRightPosition(int position) {
        if (position >= 0) {
            return position % colors.length;
        }
        return 0;
    }

    private void createAnimView() {
        ImageView imageView = getImageView();
        imageView.setScaleX(0);
        imageView.setScaleY(0);
        initData(imageView);
        startScaleAnim(imageView);
    }

    private void startScaleAnim(final ImageView imageView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                imageView.setScaleX(value);
                imageView.setScaleY(value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (position == colors.length - 1) {
                    position = 0;
                } else {
                    position++;
                }
                fixImageView.setBackgroundColor(Color.RED);
                startTransitionAnim(imageView);
            }
        });
        valueAnimator.start();
    }

    private void startTransitionAnim(final ImageView imageView) {
        Path path;
        int round = (int) (Math.random() * 100);
        if (round % 2 == 0) {
            path = createLeftPath();
        } else {
            path = createRightPath();
        }

        final PathMeasure pathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int length = (int) (pathMeasure.getLength() * value);
                pathMeasure.getPosTan(length, pos, null);
                Log.d("TAG", "onAnimationUpdate: " + pos[0] + ":" + pos[1]);
                imageView.setTranslationX(pos[0] - circleWidth * 0.5f);
                imageView.setTranslationY(pos[1] - circleHeight * 0.5f);
                imageView.setAlpha(1.0f - value);
                if (value <= 0.5f) {
                    imageView.setScaleX(1.0f - value);
                    imageView.setScaleY(1.0f - value);
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(imageView);
            }
        });
        valueAnimator.start();

    }


    private Path createLeftPath() {
        Path path = new Path();
        path.moveTo(parentWidth * 0.5f, parentHeight * 1.0f - circleHeight * 0.5f);

        controlFirstPoint.x = (int) (circleWidth * 0.5f);
        controlFirstPoint.y = (int) (parentHeight * 0.7f);
        controlSecondPoint.x = (int) (parentWidth - circleWidth * 0.5f);
        controlSecondPoint.y = (int) (parentHeight * 0.2f);

        path.cubicTo(controlFirstPoint.x, controlFirstPoint.y, controlSecondPoint.x, controlSecondPoint.y, finishPoint.x, finishPoint.y);
        return path;
    }

    private Path createRightPath() {
        Path path = new Path();
        path.moveTo(parentWidth * 0.5f, parentHeight * 1.0f - circleHeight * 0.5f);
        controlFirstPoint.x = (int) (parentWidth - circleWidth * 0.5f);
        controlFirstPoint.y = (int) (parentHeight * 0.7f);
        controlSecondPoint.x = (int) (circleWidth * 0.5f);
        controlSecondPoint.y = (int) (parentHeight * 0.2f);
        path.cubicTo(controlFirstPoint.x, controlFirstPoint.y, controlSecondPoint.x, controlSecondPoint.y, finishPoint.x, finishPoint.y);
        return path;
    }


}
