package pers.sanfen.android.swiperefreshview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import pers.sanfen.android.swiperefreshview.R;


/**
 * @Author: sanfen-mz
 * @Date: 2018/8/7
 * @Version 1.0
 */
public class LoadView extends View {

    private int orange = 0xffff4800;

    Paint paint;
    private int lineWidth;//动画粗细
    private int maxHeight;  //最大长度
    private int minHeight;  //最小长度
    private int interval;    //间隔
    long animDuration = 300;//执行一次需要的时间

    private int mWidth = 150;
    private int mHeight = 150;
    ValueAnimator val;
    float fraction = -1;
    DrawFilter drawFilter;


    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LoadView);
        lineWidth = typedArray.getInteger(R.styleable.LoadView_load_line_width, 5);
        animDuration = typedArray.getInteger(R.styleable.LoadView_load_duration, 300);
        interval = typedArray.getInteger(R.styleable.LoadView_load_interval, 20);
        typedArray.recycle();
        initPaint(context);
    }

    private void initPaint(Context context) {
        maxHeight = 50;
        minHeight = 25;
        mWidth = interval * 2 + lineWidth;
        mHeight = maxHeight;
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setColor(orange);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = right - left;
            mHeight = bottom - top;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpectMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpectSize;
        }
        if (heightSpectMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpectSize;
            maxHeight = mHeight;
            minHeight = mHeight / 2;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(drawFilter);
        if (fraction == -1 || val == null || !val.isRunning()) {
            first(canvas);
        } else {
            float dex = (maxHeight - minHeight) * fraction;
            canvas.drawLine(mWidth / 2, mHeight / 2 - maxHeight / 2 + dex / 2, mWidth / 2, mHeight / 2 + maxHeight / 2 - dex / 2, paint);
            canvas.drawLine(mWidth / 2 - interval, mHeight / 2 - minHeight / 2 - dex / 2, mWidth / 2 - interval, mHeight / 2 + minHeight / 2 + dex / 2, paint);
            canvas.drawLine(mWidth / 2 + interval, mHeight / 2 - minHeight / 2 - dex / 2, mWidth / 2 + interval, mHeight / 2 + minHeight / 2 + dex / 2, paint);
        }
    }

    public void first(Canvas canvas) {
        canvas.drawLine(mWidth / 2, mHeight / 2 - maxHeight / 2, mWidth / 2, mHeight / 2 + maxHeight / 2, paint);
        canvas.drawLine(mWidth / 2 - interval, mHeight / 2 - minHeight / 2, mWidth / 2 - interval, mHeight / 2 + minHeight / 2, paint);
        canvas.drawLine(mWidth / 2 + interval, mHeight / 2 - minHeight / 2, mWidth / 2 + interval, mHeight / 2 + minHeight / 2, paint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void performAnim() {
        if (val != null) {
            if (!val.isRunning()) {
                val.start();
            }
        } else {
            val = ValueAnimator.ofFloat(0, 1);
            val.setDuration(animDuration);
            val.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    fraction = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            val.setRepeatMode(ValueAnimator.REVERSE);
            val.setRepeatCount(ValueAnimator.INFINITE);
            val.start();
        }
    }


    public void setDuration(long animDuration) {
        this.animDuration = animDuration;
    }

    public void cancelAnim() {
        fraction = 0;
        if (val != null) {
            val.cancel();
        }
        postInvalidate();
    }

}

