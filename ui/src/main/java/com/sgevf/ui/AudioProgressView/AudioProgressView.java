package com.sgevf.ui.AudioProgressView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sgevf.ui.R;
import com.sgevf.ui.utils.DensityUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class AudioProgressView extends View {
    private final int DEFAULT_PICTURE_SIZE = DensityUtil.dip2px(getContext(), 54);
    private final int DEFAULT_TORUS_RADIUS = DensityUtil.dip2px(getContext(), 5);
    private final int DEFAULT_PROGRESS_RADIUS = DensityUtil.dip2px(getContext(), 3);
    private final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final int DEFAULT_PROGRESS_COLOR = Color.GREEN;
    private int mPictureSize;
    private int mTorusRadius;
    private int mProgressRadius;
    private int mBackgroundColor;
    private int mProgressColor;
    private int mPlayIconResId;
    private int mPauseIconResId;
    private int mImageRes;

    private String url = "";
    private Bitmap mBitmap;

    private Paint mBitmapPaint;
    private Paint mProgressPaint;
    private Paint mBackgroundPaint;
    private int size;

    private int max;
    private int progress;
    private boolean isPlaying = true;
    private boolean isRotate = false;

    private ImageAsyncTask asyncTask;
    private ValueAnimator valueAnimator;
    private float offset;


    public AudioProgressView(Context context) {
        this(context, null);
    }

    public AudioProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPaint();
        initRotateAnim();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AudioProgressView);
        try {
            mPictureSize = a.getDimensionPixelSize(R.styleable.AudioProgressView_pictureSize, DEFAULT_PICTURE_SIZE);
            mTorusRadius = a.getDimensionPixelSize(R.styleable.AudioProgressView_torusRadius, DEFAULT_TORUS_RADIUS);
            mProgressRadius = a.getDimensionPixelOffset(R.styleable.AudioProgressView_progressRadius, DEFAULT_PROGRESS_RADIUS);
            mBackgroundColor = a.getColor(R.styleable.AudioProgressView_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            mProgressColor = a.getColor(R.styleable.AudioProgressView_progressColor, DEFAULT_PROGRESS_COLOR);
            mPlayIconResId = a.getResourceId(R.styleable.AudioProgressView_playIcon, 0);
            mPauseIconResId = a.getResourceId(R.styleable.AudioProgressView_pauseIcon, 0);
            mImageRes = a.getResourceId(R.styleable.AudioProgressView_imageRes, 0);
        } finally {
            a.recycle();
        }
    }


    private void initPaint() {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setFilterBitmap(true);
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeWidth(mProgressRadius);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
    }

    private void initRotateAnim() {
        valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTorusRadius = Math.max(mTorusRadius, mProgressRadius);
        size = mPictureSize + 2 * mTorusRadius;
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawCircle(size / 2, size / 2, size / 2, mBackgroundPaint);
        //绘制图片
        drawCircleBitmap(canvas);
        //绘制进度条
        drawProgress(canvas);
        //绘制播放或者暂停
        drawPlayAndPause(canvas);
    }

    private void drawCircleBitmap(Canvas canvas) {
        canvas.save();
        if (!TextUtils.isEmpty(url) && mBitmap != null) {
            int minBitmapSize = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
            float scale = mPictureSize * 1.0f / minBitmapSize;
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postRotate(offset, size / 2, size / 2);
            BitmapShader bs = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bs.setLocalMatrix(matrix);
            mBitmapPaint.setShader(bs);
            canvas.drawCircle(size / 2, size / 2, mPictureSize / 2, mBitmapPaint);
        } else if (mImageRes != 0) {
            Bitmap bp = BitmapFactory.decodeResource(getResources(), mImageRes);
            int minBitmapSize = Math.min(bp.getWidth(), bp.getHeight());
            float scale = mPictureSize * 1.0f / minBitmapSize;
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postRotate(offset, size / 2, size / 2);
            BitmapShader bs = new BitmapShader(bp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bs.setLocalMatrix(matrix);
            mBitmapPaint.setShader(bs);
            canvas.drawCircle(size / 2, size / 2, mPictureSize / 2, mBitmapPaint);
        }
        canvas.restore();
    }

    private void drawProgress(Canvas canvas) {
        canvas.save();
        canvas.drawArc(new RectF(mTorusRadius / 2, mTorusRadius / 2, size - mTorusRadius / 2, size - mTorusRadius / 2), -90f, progress, false, mProgressPaint);
        canvas.restore();
    }

    private void drawPlayAndPause(Canvas canvas) {
        canvas.save();
        Bitmap bp = null;
        if (isPlaying) {
            if (mPlayIconResId == 0) {
                return;
            }
            bp = BitmapFactory.decodeResource(getResources(), mPauseIconResId);
        } else {
            if (mPauseIconResId == 0) {
                return;
            }
            bp = BitmapFactory.decodeResource(getResources(), mPlayIconResId);
        }
        canvas.drawBitmap(bp, size / 2 - bp.getWidth() / 2, size / 2 - bp.getHeight() / 2, mBitmapPaint);
        canvas.restore();
    }

    public void setImageUrl(String url) {
        if (!this.url.equals(url)) {
            this.url = url;
            loadNetworkImage(url);
        }
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        if (max != 0) {
            float value = progress * 1.0f / max;
            this.progress = (int) (360 * value);
        } else {
            this.progress = progress;
        }
        invalidate();
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isRotate) {
            if (isPlaying) {
                valueAnimator.start();
            } else {
                valueAnimator.pause();
            }
        }
        invalidate();
    }

    public void setRotate(boolean isRotate) {
        this.isRotate = isRotate;

    }

    private void loadNetworkImage(String url) {
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new ImageAsyncTask(this);
        asyncTask.execute(url);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    static class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        WeakReference<AudioProgressView> mView;

        ImageAsyncTask(AudioProgressView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            InputStream is = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mView.get().mBitmap = bitmap;
            if (mView.get().isPlaying && mView.get().valueAnimator != null && mView.get().isRotate) {
                mView.get().valueAnimator.start();
            }
            mView.get().invalidate();
        }
    }
}
